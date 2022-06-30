package kamenev.delivery.auth.filter;

import kamenev.delivery.auth.security.service.TokenService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
//@Component
public class AuthTokenFilter {

    private final TokenService tokenService;

//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
//                                    FilterChain filterChain) throws ServletException, IOException {
//        var tokenOpt = Utils.parseJwtToken(request);
//        if (tokenOpt.isEmpty()) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        var token = tokenOpt.get();
//        var userDetails  = tokenService.verify(token);
//        var req = wrapRequest(request, userDetails);
//        Auth auth = new Auth(
//            userDetails,
//            token,
//            userDetails.getAuthorities()
//        );
//        auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//        SecurityContextHolder.getContext().setAuthentication(auth);
//        filterChain.doFilter(req, response);
//    }

//    private HttpServletRequestWrapper wrapRequest(HttpServletRequest request, UserDetails userDetails) {
//        var req = RequestWrapper.of(request);
//        req.addHeader("id", userDetails.getId().toString());
//        req.addHeader("email", userDetails.getEmail());
//        req.addHeader("permissions", userDetails.stringifyAuthorities());
//        return req;
//    }
}
