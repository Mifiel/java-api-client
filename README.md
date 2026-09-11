# java-api-client

Java SDK for the [Mifiel](https://www.mifiel.com) API.

## Documentation

API reference, guides, and examples:

- English: https://docs.mifiel.com/en/
- Español: https://docs.mifiel.com/es/

This README covers installation and client setup only.

## Installation

TODO

## Setup

1. Create an account (production or [sandbox](https://app-sandbox.mifiel.com)).
2. Generate an `APP_ID` and `APP_SECRET` in [Access Tokens](https://app-sandbox.mifiel.com/settings/access-tokens).
3. Configure the client:

```java
import com.mifiel.api.ApiClient;

ApiClient apiClient = new ApiClient(appId, appSecret);
// Production is the default (https://app.mifiel.com).
// For sandbox:
apiClient.setUrl("https://app-sandbox.mifiel.com");
```

## Contributing

1. Fork it (https://github.com/Mifiel/java-api-client/fork)
2. Create your feature branch (`git checkout -b my-new-feature`)
3. Commit your changes (`git commit -am 'Add some feature'`)
4. Push to the branch (`git push origin my-new-feature`)
5. Create a new Pull Request
