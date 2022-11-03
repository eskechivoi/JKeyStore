# JKeyStore
Java key store, in CLI mode. Use a passphrase to protect your passwords!

## Installation - CLI Mode

If you want to use JKeyStore in CLI mode, just download the src code and compile with the following line, on src's father folder:

    javac -g -d bin -cp src src/password/*.java
    javac -g -d bin -cp src src/cli/*.java

You can also run the `compiler.sh` script.

## Use - CLI Mode

Now, go to your `bin` folder, and type the following line:

    java cli.StoreInterface

This will open a CLI client to use the Key Store. You can also run the `run.sh` script. Both scripts work for Windows and for Linux.
