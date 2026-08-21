# Changelog

## v2.0.0 - 2026-08-21

### Breaking changes

- Default API host changed from `https://www.mifiel.com` to `https://app.mifiel.com`.
- Sandbox documentation and examples now use `https://app-sandbox.mifiel.com` instead of `https://sandbox.mifiel.com`.

### Migration

```java
import com.mifiel.api.ApiClient;

ApiClient apiClient = new ApiClient(appId, appSecret);
// Production requests now go to https://app.mifiel.com/api/v1/...

// Sandbox
apiClient.setUrl("https://app-sandbox.mifiel.com");
```

If you previously set a legacy host explicitly, update it or remove the override to use the new default.
