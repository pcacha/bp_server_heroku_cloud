package cz.zcu.students.cacha.bp_server.security;

/**
 * Class holding constants used in security
 */
public class SecurityConstants {
    /**
     * jwt generation secret
     */
    public static final String SECRET = "73cb89658e2784ca81553edf3c7c063edbb39f7d";
    /**
     * http authorization token prefix
     */
    public static final String TOKEN_PREFIX = "Bearer ";
    /**
     * http authorization
     */
    public static final String HEADER_STRING = "Authorization";
    /**
     * jwt expiration length
     */
    public static final long EXPIRATION_TIME = 604_800_000 ;
}
