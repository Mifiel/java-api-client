# java-api-client

Mifiel API Client for Java

Java SDK for [Mifiel](https://www.mifiel.com) API.
Please read our [documentation](http://docs.mifiel.com/) for instructions on how to start using the API.

## Installation

TODO

## Usage

For your convenience Mifiel offers a Sandbox environment where you can confidently test your code.

To start using the API in the Sandbox environment you need to first create an account at [sandbox.mifiel.com](https://sandbox.mifiel.com).

Once you have an account you will need an APP_ID and an APP_SECRET which you can generate in [sandbox.mifiel.com/access_tokens](https://sandbox.mifiel.com/access_tokens).

Then you can configure the library with:

```java
  import com.mifiel.api.ApiClient;

  ApiClient apiClient = new ApiClient(appId, appSecret);
  // if you want to use our sandbox environment use:
  apiClient.setUrl("https://sandbox.mifiel.com");
```

Document methods:

- Find:

  ```java
    import com.mifiel.api.dao.Documents;
    import com.mifiel.api.objects.Document;

    Documents documents = new Documents(apiClient);
    Document document = documents.find("id");
    document.getOriginalHash();
    document.getFile();
    document.getFileSigned();
    // ...
  ```

- Find all:

  ```java
    import java.util.List;
    import com.mifiel.api.dao.Documents;
    import com.mifiel.api.objects.Document;

    Documents documents = new Documents(apiClient);
    List<Document> allDocuments = documents.findAll();
  ```

- Create:

> Use only **original_hash** if you dont want us to have the file.<br>
> Only **file** or **original_hash** must be provided.

```java
  import java.util.List;
  import java.util.ArrayList;
  import com.mifiel.api.dao.Documents;
  import com.mifiel.api.objects.Document;
  import com.mifiel.api.objects.Signature;
  import com.mifiel.api.utils.MifielUtils;

  Documents documents = new Documents(apiClient);
  Document document = new Document();
  document.setFile("path/to/my-file.pdf");

  List<Signature> signatures = new ArrayList<Signature>();
  Signature signature1 = new Signature();
  Signature signature2 = new Signature();

  signature1.setSignature("Signer 1");
  signature1.setEmail("signer1@email.com");
  signature1.setTaxId("AAA010101AAA");

  signature2.setSignature("Signer 2");
  signature2.setEmail("signer2@email.com");
  signature2.setTaxId("AAA010102AAA");

  signatures.add(signature1);
  signatures.add(signature2);

  document.setSignatures(signatures);
  documents.save(document);

  // if you dont want us to have the PDF, you can just send us
  // the original_hash and the name of the document. Both are required
  Document document2 = new Document();
  document2.setOriginalHash(MifielUtils.getDocumentHash("path/to/my-file.pdf"));
  document2.setSignatures(signatures);

  documents.save(document2);
```

- Save signed files

```java
  import com.mifiel.api.dao.Documents;

  Documents documents = new Documents(apiClient);

  // download the signed pdf
  documents.saveSignedFile("id", "path/to/save/file-signed.pdf");
  // download the signed xml file
  documents.saveXml("id", "path/to/save/xml.xml");
  // download zip file containing the signed pdf and the xml
  documents.saveZip("id", "path/to/save/xml.xml");
```

- Delete

  ```java
    import com.mifiel.api.dao.Documents;
    import com.mifiel.api.objects.Document;

    Documents documents = new Documents(apiClient);
    documents.delete("id");
  ```
