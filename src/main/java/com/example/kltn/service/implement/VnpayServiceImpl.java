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

// import javax.servlet.http.HttpServletRequest;

@Service
@RequiredArgsConstructor
@Slf4j
public class VnpayServiceImpl implements VnpayService {

    private final VnpayConfig vnpayConfig;

    private static final String VNP_TMNCODE = "QX6M7DBQ";
    private static final String VNP_HASHSECRET = "BT0SSSR5PF7UL3VYXFIROC20FTLWD48F";
    private static final String VNP_URL = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";

    @Override
    public String createPayment(String orderId, String amount, String orderInfo) {
        try {
            Map<String, String> vnpParams = new HashMap<>();
            vnpParams.put("vnp_Version", "2.1.0");
            vnpParams.put("vnp_Command", "pay");
            vnpParams.put("vnp_TmnCode", VNP_TMNCODE);
            vnpParams.put("vnp_Amount", String.valueOf(Long.parseLong(amount) * 100));
            vnpParams.put("vnp_CurrCode", "VND");
            vnpParams.put("vnp_TxnRef", orderId);
            vnpParams.put("vnp_OrderInfo", orderInfo);
            vnpParams.put("vnp_Locale", "vn");
            vnpParams.put("vnp_ReturnUrl", "http://localhost:8521/KLTN_DEPLOY_RAILWAY/api/v1/payments/vnpay-return");
            vnpParams.put("vnp_IpAddr", "127.0.0.1");
            vnpParams.put("vnp_CreateDate", new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date()));

            String query = vnpParams.entrySet().stream()
                .map(entry -> URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8) + "=" + URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8))
                .collect(Collectors.joining("&"));

            String secureHash = hmacSHA512(VNP_HASHSECRET, vnpParams);
            String paymentUrl = VNP_URL + "?" + query + "&vnp_SecureHash=" + secureHash;

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

    @Override
    public String hmacSHA512(String key, Map<String, String> params) {
        try {
            String data = params.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining("&"));

            javax.crypto.Mac hmac = javax.crypto.Mac.getInstance("HmacSHA512");
            javax.crypto.spec.SecretKeySpec secretKeySpec = new javax.crypto.spec.SecretKeySpec(key.getBytes(), "HmacSHA512");
            hmac.init(secretKeySpec);
            byte[] hash = hmac.doFinal(data.getBytes());
            return org.apache.commons.codec.binary.Hex.encodeHexString(hash);
        } catch (Exception e) {
            log.error("Error calculating HMAC SHA512: ", e);
            throw new RuntimeException("Error calculating HMAC SHA512: " + e.getMessage());
        }
    }

    // private String getRandomNumber(int length) {
    //     // Implement random number generation logic
    //     return "12345678";
    // }

    // private String getIpAddress(HttpServletRequest request) {
    //     String ipAddress = request.getHeader("X-Forwarded-For");
    //     if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
    //         ipAddress = request.getHeader("Proxy-Client-IP");
    //     }
    //     if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
    //         ipAddress = request.getHeader("WL-Proxy-Client-IP");
    //     }
    //     if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
    //         ipAddress = request.getRemoteAddr();
    //     }
    //     return ipAddress;
    // }
}
