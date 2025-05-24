package com.whatsapp.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.List;

@Component
public class WebSocketAuthChannelInterceptor implements ChannelInterceptor {

    @Value(JwtConstant.SECRET_KEY)
    private String secretKey; // Load secret key from application.properties or use JwtConstant

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if (accessor.getCommand() != null && accessor.getCommand().toString().equals("CONNECT")) {
            List<String> authHeaders = accessor.getNativeHeader("Authorization");

            if (authHeaders != null && !authHeaders.isEmpty()) {
                String token = authHeaders.get(0).replace("Bearer ", "");

                try {
                    SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes());
                    Claims claim = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();

                    String username = String.valueOf(claim.get("email"));
                    String authorities = String.valueOf(claim.get("authorities"));
                    List<GrantedAuthority> auths = AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);

                    Authentication auth = new UsernamePasswordAuthenticationToken(username, null, auths);
                    accessor.setUser(auth);
                } catch (Exception e) {
                    throw new BadCredentialsException("Invalid token received", e);
                }
            } else {
                throw new BadCredentialsException("Missing Authorization header in STOMP CONNECT");
            }
        }

        return message;
    }
}
