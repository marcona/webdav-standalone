package com.marc.onnet;
import com.bradmcevoy.http.AuthenticationHandler;
import com.bradmcevoy.http.FilterChain;
import com.bradmcevoy.http.Request;
import com.bradmcevoy.http.Request.Method;
import com.bradmcevoy.http.Response;
import com.bradmcevoy.http.http11.Http11ResponseHandler;
import java.util.List;
/**
 *
 */
public class PreAuthenticationFilter extends
                                     com.bradmcevoy.http.http11.auth.PreAuthenticationFilter {

    public PreAuthenticationFilter(Http11ResponseHandler responseHandler,
                                   List<AuthenticationHandler> authenticationHandlers) {
        super(responseHandler, authenticationHandlers);
    }


    @Override
    public void process(FilterChain chain, Request request, Response
          response) {
        /** workaround to allow unauthenticated OPTIONS request from
         e.g. MS Windows */
        if ((request.getMethod().equals(Method.OPTIONS))
            && (request.getAuthorization() == null)) {
            chain.process(request, response);
        }
        else {
            super.process(chain, request, response);
        }
    }
}

