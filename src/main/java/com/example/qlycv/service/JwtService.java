package com.example.qlycv.service;

import java.security.Key;
import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import com.example.qlycv.auth.CustomUserDetails;
import com.example.qlycv.common.Common;
import com.example.qlycv.entity.User;
import com.example.qlycv.repository.UserRepo;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtService {

	@Autowired
	private UserRepo userRepo;

	public static final String SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";

	public static final int JWT_EXPIRATION = 8; // h

	public String generateToken(CustomUserDetails userDetails) {
		// create jwt with userName
		return Jwts.builder().setSubject(userDetails.getUsername()).setIssuedAt(new Date())
				.setExpiration(Common.addTime(new Date(), Calendar.HOUR_OF_DAY, JWT_EXPIRATION))
				.signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
	}

	private Key getSignKey() {
		byte[] keyBytes = Decoders.BASE64.decode(SECRET);
		return Keys.hmacShaKeyFor(keyBytes);
	}

	public String getUserNameFromJWT(String token) {
		return Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token).getBody().getSubject();
	}

	public Boolean validateToken(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token);
			return true;
		} catch (Exception ex) {
			throw ex;
		}
	}

	public User getUserAuthLog() {
		User user = new User();
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!ObjectUtils.isEmpty(authentication)) {
			String jwt = generateToken((CustomUserDetails) authentication.getPrincipal());
			String userName = getUserNameFromJWT(jwt);
			user = userRepo.findByUserName(userName);
		}

		return user;
	}

}
