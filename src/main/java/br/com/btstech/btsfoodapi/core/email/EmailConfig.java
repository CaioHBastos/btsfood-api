package br.com.btstech.btsfoodapi.core.email;

import br.com.btstech.btsfoodapi.domain.service.EnvioEmailService;
import br.com.btstech.btsfoodapi.infrastructure.service.email.FakeEnvioEmailService;
import br.com.btstech.btsfoodapi.infrastructure.service.email.SandboxEnvioEmailService;
import br.com.btstech.btsfoodapi.infrastructure.service.email.SmtpEnvioEmailService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@AllArgsConstructor
@Configuration
public class EmailConfig {

    private EmailProperties emailProperties;

    @Bean
    public EnvioEmailService envioEmailService() {
        // Acho melhor usar switch aqui do que if/else if
        switch (emailProperties.getImpl()) {
            case FAKE:
                return new FakeEnvioEmailService();
            case SMTP:
                return new SmtpEnvioEmailService();
            case SANDBOX:
                return new SandboxEnvioEmailService();
            default:
                return null;
        }
    }
}