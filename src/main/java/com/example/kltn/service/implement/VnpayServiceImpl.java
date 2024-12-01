package com.example.kltn.service.implement;

import com.example.kltn.config.VnpayConfig;
import com.example.kltn.service.VnpayService;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.HashMap;
import java.util.UUID;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class VnpayServiceImpl implements VnpayService {

    private final VnpayConfig vnpayConfig;

    @Override
    public String createPayment(String orderId, String amount, String orderInfo) {
        try {
            Map<String, String> vnpParams = new HashMap<>();
            vnpParams.put("vnp_Version", "2.1.0");
            vnpParams.put("vnp_Command", "pay");
            vnpParams.put("vnp_TmnCode", vnpayConfig.getTmnCode());
            vnpParams.put("vnp_Amount", String.valueOf(Long.parseLong(amount) * 100));
            vnpParams.put("vnp_CurrCode", "VND");
            vnpParams.put("vnp_TxnRef", orderId);
            vnpParams.put("vnp_OrderInfo", orderInfo);
            vnpParams.put("vnp_Locale", "vn");
            vnpParams.put("vnp_ReturnUrl", vnpayConfig.getReturnUrl());
            vnpParams.put("vnp_IpAddr", "127.0.0.1");
            vnpParams.put("vnp_CreateDate", new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date()));

            String query = vnpParams.entrySet().stream()
                .map(entry -> URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8) + "=" + URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8))
                .collect(Collectors.joining("&"));

            String secureHash = hmacSHA512(vnpayConfig.getHashSecret(), query);
            String paymentUrl = vnpayConfig.getApiUrl() + "?" + query + "&vnp_SecureHash=" + secureHash;

            log.info("VNPAY Payment URL: {}", paymentUrl);
            return paymentUrl;
        } catch (Exception e) {
            log.error("Error creating VNPAY payment: ", e);
            throw new RuntimeException("Error creating VNPAY payment: " + e.getMessage());
        }
    }

    @Override
    public boolean processPaymentNotify(Map<String, String> notifyParams) {
        // Implement logic to process VNPAY payment notification
        return false;
    }

    private String hmacSHA512(String key, String data) throws Exception {
        javax.crypto.Mac hmac = javax.crypto.Mac.getInstance("HmacSHA512");
        javax.crypto.spec.SecretKeySpec secretKeySpec = new javax.crypto.spec.SecretKeySpec(key.getBytes(), "HmacSHA512");
        hmac.init(secretKeySpec);
        byte[] hash = hmac.doFinal(data.getBytes());
        return org.apache.commons.codec.binary.Hex.encodeHexString(hash);
    }
}
