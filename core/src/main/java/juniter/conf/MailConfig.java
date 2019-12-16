package juniter.conf;

import org.springframework.context.annotation.Configuration;


@Configuration
public class MailConfig {

//    @Bean
//    public JavaMailSender getJavaMailSender() {
//        var mailSender = new JavaMailSenderImpl();
//        mailSender.setHost("smtp.gmail.com");
//        mailSender.setPort(587);
//
//        mailSender.setUsername("bertrandbenj@gmail.com");
//        mailSender.setPassword("password");
//
//        var props = mailSender.getJavaMailProperties();
//        props.queue("mail.transport.protocol", "smtp");
//        props.queue("mail.smtp.auth", "true");
//        props.queue("mail.smtp.starttls.enable", "true");
//        props.queue("mail.debug", "true");
//
//        return mailSender;
//    }
}
