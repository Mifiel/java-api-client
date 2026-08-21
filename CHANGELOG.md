# Changelog

## v2.0.0 - 2026-08-21

### Breaking changes

- Default API host changed from `https://www.mifiel.com` to `https://app.mifiel.com`.
- Sandbox documentation and examples now use `https://app-sandbox.mifiel.com` instead of `https://sandbox.mifiel.com`.

### Features

- Send a standardized `User-Agent` on API requests, e.g. `JAVA/17.0.9 mifiel-api-client/2.0.0 httpclient/4.5.2 (Linux/6.8.0)`.

### Migration

```java
import com.mifiel.api.ApiClient;

ApiClient apiClient = new ApiClient(appId, appSecret);
// Production requests now go to https://app.mifiel.com/api/v1/...

// Sandbox
apiClient.setUrl("https://app-sandbox.mifiel.com");
```

If you previously set a legacy host explicitly, update it or remove the override to use the new default.
