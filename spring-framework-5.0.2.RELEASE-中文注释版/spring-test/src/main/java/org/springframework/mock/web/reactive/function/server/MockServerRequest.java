/*
 * Copyright 2002-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.mock.web.reactive.function.server;

import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.Charset;
import java.security.Principal;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalLong;
import java.util.concurrent.ConcurrentHashMap;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRange;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.server.PathContainer;
import org.springframework.http.server.RequestPath;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyExtractor;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.WebSession;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Mock implementation of {@link ServerRequest}.
 *
 * @author Arjen Poutsma
 * @since 5.0
 */
public class MockServerRequest implements ServerRequest {

	private final HttpMethod method;

	private final URI uri;

	private final RequestPath pathContainer;

	private final MockHeaders headers;

	private final MultiValueMap<String, HttpCookie> cookies;

	@Nullable
	private final Object body;

	private final Map<String, Object> attributes;

	private final MultiValueMap<String, String> queryParams;

	private final Map<String, String> pathVariables;

	@Nullable
	private final WebSession session;

	@Nullable
	private Principal principal;


	private MockServerRequest(HttpMethod method, URI uri, String contextPath, MockHeaders headers,
			MultiValueMap<String, HttpCookie> cookies, @Nullable Object body,
			Map<String, Object> attributes, MultiValueMap<String, String> queryParams,
			Map<String, String> pathVariables, @Nullable WebSession session, @Nullable Principal principal) {

		this.method = method;
		this.uri = uri;
		this.pathContainer = RequestPath.parse(uri, contextPath);
		this.headers = headers;
		this.cookies = cookies;
		this.body = body;
		this.attributes = attributes;
		this.queryParams = queryParams;
		this.pathVariables = pathVariables;
		this.session = session;
		this.principal = principal;
	}


	@Override
	public HttpMethod method() {
		return this.method;
	}

	@Override
	public String methodName() {
		return this.method.name();
	}

	@Override
	public URI uri() {
		return this.uri;
	}

	@Override
	public UriBuilder uriBuilder() {
		return UriComponentsBuilder.fromHttpRequest(new ServerRequestAdapter());
	}

	@Override
	public PathContainer pathContainer() {
		return this.pathContainer;
	}

	@Override
	public Headers headers() {
		return this.headers;
	}

	@Override
	public MultiValueMap<String, HttpCookie> cookies() {
		return this.cookies;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <S> S body(BodyExtractor<S, ? super ServerHttpRequest> extractor) {
		Assert.state(this.body != null, "No body");
		return (S) this.body;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <S> S body(BodyExtractor<S, ? super ServerHttpRequest> extractor, Map<String, Object> hints) {
		Assert.state(this.body != null, "No body");
		return (S) this.body;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <S> Mono<S> bodyToMono(Class<? extends S> elementClass) {
		Assert.state(this.body != null, "No body");
		return (Mono<S>) this.body;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <S> Mono<S> bodyToMono(ParameterizedTypeReference<S> typeReference) {
		Assert.state(this.body != null, "No body");
		return (Mono<S>) this.body;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <S> Flux<S> bodyToFlux(Class<? extends S> elementClass) {
		Assert.state(this.body != null, "No body");
		return (Flux<S>) this.body;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <S> Flux<S> bodyToFlux(ParameterizedTypeReference<S> typeReference) {
		Assert.state(this.body != null, "No body");
		return (Flux<S>) this.body;
	}

	@Override
	public Map<String, Object> attributes() {
		return this.attributes;
	}

	@Override
	public MultiValueMap<String, String> queryParams() {
		return CollectionUtils.unmodifiableMultiValueMap(this.queryParams);
	}

	@Override
	public Map<String, String> pathVariables() {
		return Collections.unmodifiableMap(this.pathVariables);
	}

	@Override
	public Mono<WebSession> session() {
		return Mono.justOrEmpty(this.session);
	}

	@Override
	public Mono<? extends Principal> principal() {
		return Mono.justOrEmpty(this.principal);
	}

	public static Builder builder() {
		return new BuilderImpl();
	}

	/**
	 * Builder for {@link MockServerRequest}.
	 */
	public interface Builder {

		Builder method(HttpMethod method);

		Builder uri(URI uri);

		Builder contextPath(String contextPath);

		Builder header(String key, String value);

		Builder headers(HttpHeaders headers);

		Builder cookie(HttpCookie... cookies);

		Builder cookies(MultiValueMap<String, HttpCookie> cookies);

		Builder attribute(String name, Object value);

		Builder attributes(Map<String, Object> attributes);

		Builder queryParam(String key, String value);

		Builder queryParams(MultiValueMap<String, String> queryParams);

		Builder pathVariable(String key, String value);

		Builder pathVariables(Map<String, String> pathVariables);

		Builder session(WebSession session);

		Builder session(Principal principal);

		MockServerRequest body(Object body);

		MockServerRequest build();
	}


	private static class BuilderImpl implements Builder {

		private HttpMethod method = HttpMethod.GET;

		private URI uri = URI.create("http://localhost");

		private String contextPath = "";

		private MockHeaders headers = new MockHeaders(new HttpHeaders());

		private MultiValueMap<String, HttpCookie> cookies = new LinkedMultiValueMap<>();

		@Nullable
		private Object body;

		private Map<String, Object> attributes = new ConcurrentHashMap<>();

		private MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();

		private Map<String, String> pathVariables = new LinkedHashMap<>();

		@Nullable
		private WebSession session;

		@Nullable
		private Principal principal;

		@Override
		public Builder method(HttpMethod method) {
			Assert.notNull(method, "'method' must not be null");
			this.method = method;
			return this;
		}

		@Override
		public Builder uri(URI uri) {
			Assert.notNull(uri, "'uri' must not be null");
			this.uri = uri;
			return this;
		}

		@Override
		public Builder contextPath(String contextPath) {
			Assert.notNull(contextPath, "'contextPath' must not be null");
			this.contextPath = contextPath;
			return this;

		}

		@Override
		public Builder cookie(HttpCookie... cookies) {
			Arrays.stream(cookies).forEach(cookie -> this.cookies.add(cookie.getName(), cookie));
			return this;
		}

		@Override
		public Builder cookies(MultiValueMap<String, HttpCookie> cookies) {
			Assert.notNull(cookies, "'cookies' must not be null");
			this.cookies = cookies;
			return this;
		}

		@Override
		public Builder header(String key, String value) {
			Assert.notNull(key, "'key' must not be null");
			Assert.notNull(value, "'value' must not be null");
			this.headers.header(key, value);
			return this;
		}

		@Override
		public Builder headers(HttpHeaders headers) {
			Assert.notNull(headers, "'headers' must not be null");
			this.headers = new MockHeaders(headers);
			return this;
		}

		@Override
		public Builder attribute(String name, Object value) {
			Assert.notNull(name, "'name' must not be null");
			Assert.notNull(value, "'value' must not be null");
			this.attributes.put(name, value);
			return this;
		}

		@Override
		public Builder attributes(Map<String, Object> attributes) {
			Assert.notNull(attributes, "'attributes' must not be null");
			this.attributes = attributes;
			return this;
		}

		@Override
		public Builder queryParam(String key, String value) {
			Assert.notNull(key, "'key' must not be null");
			Assert.notNull(value, "'value' must not be null");
			this.queryParams.add(key, value);
			return this;
		}

		@Override
		public Builder queryParams(MultiValueMap<String, String> queryParams) {
			Assert.notNull(queryParams, "'queryParams' must not be null");
			this.queryParams = queryParams;
			return this;
		}

		@Override
		public Builder pathVariable(String key, String value) {
			Assert.notNull(key, "'key' must not be null");
			Assert.notNull(value, "'value' must not be null");
			this.pathVariables.put(key, value);
			return this;
		}

		@Override
		public Builder pathVariables(Map<String, String> pathVariables) {
			Assert.notNull(pathVariables, "'pathVariables' must not be null");
			this.pathVariables = pathVariables;
			return this;
		}

		@Override
		public Builder session(WebSession session) {
			Assert.notNull(session, "'session' must not be null");
			this.session = session;
			return this;
		}

		@Override
		public Builder session(Principal principal) {
			Assert.notNull(principal, "'principal' must not be null");
			this.principal = principal;
			return this;
		}

		@Override
		public MockServerRequest body(Object body) {
			this.body = body;
			return new MockServerRequest(this.method, this.uri, this.contextPath, this.headers,
					this.cookies, this.body, this.attributes, this.queryParams, this.pathVariables,
					this.session, this.principal);
		}

		@Override
		public MockServerRequest build() {
			return new MockServerRequest(this.method, this.uri, this.contextPath, this.headers,
					this.cookies, null, this.attributes, this.queryParams, this.pathVariables,
					this.session, this.principal);
		}
	}


	private static class MockHeaders implements Headers {

		private final HttpHeaders headers;

		public MockHeaders(HttpHeaders headers) {
			this.headers = headers;
		}

		private HttpHeaders delegate() {
			return this.headers;
		}

		public void header(String key, String value) {
			this.headers.add(key, value);
		}

		@Override
		public List<MediaType> accept() {
			return delegate().getAccept();
		}

		@Override
		public List<Charset> acceptCharset() {
			return delegate().getAcceptCharset();
		}

		@Override
		public List<Locale.LanguageRange> acceptLanguage() {
			return delegate().getAcceptLanguage();
		}

		@Override
		public OptionalLong contentLength() {
			return toOptionalLong(delegate().getContentLength());
		}

		@Override
		public Optional<MediaType> contentType() {
			return Optional.ofNullable(delegate().getContentType());
		}

		@Override
		public InetSocketAddress host() {
			return delegate().getHost();
		}

		@Override
		public List<HttpRange> range() {
			return delegate().getRange();
		}

		@Override
		public List<String> header(String headerName) {
			List<String> headerValues = delegate().get(headerName);
			return headerValues != null ? headerValues : Collections.emptyList();
		}

		@Override
		public HttpHeaders asHttpHeaders() {
			return HttpHeaders.readOnlyHttpHeaders(delegate());
		}

		private OptionalLong toOptionalLong(long value) {
			return value != -1 ? OptionalLong.of(value) : OptionalLong.empty();
		}

	}

	private final class ServerRequestAdapter implements HttpRequest {

		@Override
		public String getMethodValue() {
			return methodName();
		}

		@Override
		public URI getURI() {
			return MockServerRequest.this.uri;
		}

		@Override
		public HttpHeaders getHeaders() {
			return MockServerRequest.this.headers.headers;
		}
	}


}
