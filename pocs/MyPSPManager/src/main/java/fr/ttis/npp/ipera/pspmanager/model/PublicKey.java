package fr.ttis.npp.ipera.pspmanager.model;

@Data
public class PublicKey {
    private String kty; // Key type
    private String use; // Usage
    private String alg; // Algorithm
    private String kid; // Key ID
    private String n;   // Public key modulus
    private String e;   // Public key exponent
}
