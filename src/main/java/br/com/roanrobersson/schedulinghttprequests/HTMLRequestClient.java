package br.com.roanrobersson.schedulinghttprequests;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = "enigma", url = "${shr.request.url}")
public interface HTMLRequestClient {

	@GetMapping
	String get();
}