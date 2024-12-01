package com.example.kltn.service.implement;

import com.example.kltn.service.MomoService;
import com.example.kltn.config.MomoConfig;
import org.springframework.stereotype.Service;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.HashMap;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.UUID;
import org.apache.commons.codec.binary.Hex;

@Service
@RequiredArgsConstructor
@Slf4j
public class MomoServiceImpl implements MomoService {
    
    private final MomoConfig momoConfig;
    private final RestTemplate restTemplate;
    
    @Override
    public String createPayment(String orderId, String amount, String orderInfo) {
        try {
            String requestId = UUID.randomUUID().toString();
            String rawSignature = "accessKey=" + momoConfig.getAccessKey() +
                    "&amount=" + amount +
                    "&extraData=" +
                    "&ipnUrl=" + momoConfig.getNotifyUrl() +
                    "&orderId=" + orderId +
                    "&orderInfo=" + orderInfo +
                    "&partnerCode=" + momoConfig.getPartnerCode() +
                    "&redirectUrl=" + momoConfig.getReturnUrl() +
                    "&requestId=" + requestId +
                    "&requestType=captureWallet";

            String signature = generateSignature(rawSignature, momoConfig.getSecretKey());

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("partnerCode", momoConfig.getPartnerCode());
            requestBody.put("requestId", requestId);
            requestBody.put("amount", Long.parseLong(amount));
            requestBody.put("orderId", orderId);
            requestBody.put("orderInfo", orderInfo);
            requestBody.put("redirectUrl", momoConfig.getReturnUrl());
            requestBody.put("ipnUrl", momoConfig.getNotifyUrl());
            requestBody.put("requestType", "captureWallet");
            requestBody.put("extraData", "");
            requestBody.put("signature", signature);
            requestBody.put("lang", "vi");
            requestBody.put("partnerName", "EventEase");
            requestBody.put("storeId", "EventEaseStore");
            requestBody.put("autoCapture", true);
            requestBody.put("orderGroupId", "");

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

            log.info("Gọi API Momo với payload: {}", requestBody);

            ResponseEntity<Map> response = restTemplate.postForEntity(
                momoConfig.getEndpoint(),
                request,
                Map.class
            );

            if (response != null 
                && response.getStatusCode() == HttpStatus.OK 
                && response.hasBody()
                && response.getBody() != null 
                && response.getBody().get("payUrl") != null) {
                String payUrl = response.getBody().get("payUrl").toString();
                log.info("PayUrl: {}", payUrl);
                return payUrl;
            } else {
                throw new RuntimeException("Không nhận được payUrl từ MoMo");
            }

        } catch (Exception e) {
            log.error("Lỗi khi tạo thanh toán MoMo: ", e);
            throw new RuntimeException("Lỗi khi tạo thanh toán: " + e.getMessage());
        }
    }

    @Override
    public boolean processPaymentNotify(Map<String, String> notifyParams) {
        try {
            log.info("Nhận thông báo thanh toán từ MoMo: {}", notifyParams);
            
            if (notifyParams != null 
                && "0".equals(notifyParams.get("resultCode"))) {
                // Xử lý thông báo thành công
                return true;
            }
            return false;
        } catch (Exception e) {
            log.error("Lỗi khi xử lý thông báo từ MoMo: ", e);
            return false;
        }
    }

    private String generateSignature(String message, String secretKey) throws Exception {
        Mac hmac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), "HmacSHA256");
        hmac.init(secretKeySpec);
        byte[] hash = hmac.doFinal(message.getBytes());
        return Hex.encodeHexString(hash);
    }
} 