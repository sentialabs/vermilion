# Proof of concept
### Embedding scripting in Bloomreach content

This is a working demonstration project that shows how you can create your own scripting language parser using the Antlr4 tool and implement the evaluation of simple code fragments inside of document content when the webpagee is rendered.


So as that you don't have to find out everything for yourself, I have added so technical documentation explaining what, how and somethimes even why. See Markdown files in the /doc directory.

In addition I have written a high level overview blog entry that explains things in a more general manner. See here:

xxx

As usual with Bloomreach projects you can get it up and running with the cargo run:

```
   mvn clean package
   mvn -P cargo.run
```

Point your browser to http://localhost:8080/site and /cms for the web application (password is admin/admin).

