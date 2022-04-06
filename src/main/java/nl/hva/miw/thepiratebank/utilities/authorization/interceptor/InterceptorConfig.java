
package nl.hva.miw.thepiratebank.utilities.authorization.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {


    private final UserIdInterceptor userIdInterceptor;
    private final AdminInterceptor adminInterceptor;

    @Autowired
    public InterceptorConfig(UserIdInterceptor userIdInterceptor, AdminInterceptor adminInterceptor) {
        this.userIdInterceptor = userIdInterceptor;
        this.adminInterceptor = adminInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userIdInterceptor);
        registry.addInterceptor(adminInterceptor);
    }
}

