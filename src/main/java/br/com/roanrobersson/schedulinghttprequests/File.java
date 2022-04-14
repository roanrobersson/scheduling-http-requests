package br.com.roanrobersson.schedulinghttprequests;

import java.io.InputStream;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
class File {

	private String name;
	private String contentType;
	private InputStream inputStream;
}