package cz.zcu.students.cacha.bp_server.security;

import cz.zcu.students.cacha.bp_server.domain.User;
import cz.zcu.students.cacha.bp_server.services.AuthUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static cz.zcu.students.cacha.bp_server.security.SecurityConstants.HEADER_STRING;
import static cz.zcu.students.cacha.bp_server.security.SecurityConstants.TOKEN_PREFIX;

/**
 * Class configurating spring security filter chain to use jwt
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private AuthUserService authUserService;

    /**
     * Adds filter to spring security that checks user provided jwt
     * @param httpServletRequest http request
     * @param httpServletResponse http response
     * @param filterChain filter chain
     * @throws ServletException exception
     * @throws IOException exception
     */
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        try {
            // get jwt
            String jwt = getJWTFromRequest(httpServletRequest);

            if(StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
                // if user has valid token find him by id
                Long userId = tokenProvider.getUserIdFromJWT(jwt);
                User userDetails = authUserService.loadUserById(userId);

                if(userDetails.isEnabled()) {
                    // if user is enabled log him in the security context
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());

                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }
        catch (Exception ex) {
            logger.error("Could not set user authentication in security context", ex);
        }

        // start next part in chain
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    /**
     * Returns jwt from http request
     * @param request http request
     * @return jwt
     */
    private String getJWTFromRequest(HttpServletRequest request) {
        // get authorization string
        String bearerToken = request.getHeader(HEADER_STRING);

        // return jwt form authorization without prefix
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith(TOKEN_PREFIX)) {
            return bearerToken.substring(7);
        }

        return null;
    }
}
