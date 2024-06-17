package com.dataslab.vscan.config.misc;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.ip.tcp.connection.AbstractServerConnectionFactory;
import org.springframework.integration.ip.tcp.connection.TcpNioServerConnectionFactory;
import org.springframework.integration.syslog.RFC5424MessageConverter;
import org.springframework.integration.syslog.inbound.RFC6587SyslogDeserializer;
import org.springframework.integration.syslog.inbound.TcpSyslogReceivingChannelAdapter;
import org.springframework.messaging.MessageChannel;

@Configuration
public class TcpIntegrationConfig {


    @Value("${tcp.server.port}")
    private int port;

    public static final String TCP_SYSLOG_CHANNEL = "tcp-channel";

    @Bean
    public AbstractServerConnectionFactory serverConnectionFactory(RFC6587SyslogDeserializer deserializer) {

        TcpNioServerConnectionFactory tcpNioServerConnectionFactory = new TcpNioServerConnectionFactory(port);
        tcpNioServerConnectionFactory.setDeserializer(deserializer);

        return tcpNioServerConnectionFactory;
    }

    @Bean(name = TCP_SYSLOG_CHANNEL)
    public MessageChannel messageChannel() {
        return new DirectChannel();
    }

    @Bean
    @Primary
    public RFC5424MessageConverter rfc5424MessageConverter() {
        var converter = new RFC5424MessageConverter();
        converter.setAsMap(true);
        return converter;
    }

    @Bean
    @Primary
    public RFC6587SyslogDeserializer rFC6587SyslogDeserializer() {
        return new RFC6587SyslogDeserializer();
    }

    @Bean
    public TcpSyslogReceivingChannelAdapter tcpSyslogReceivingChannelAdapter(AbstractServerConnectionFactory serverConnectionFactory,
                                                                             @Qualifier(value = TCP_SYSLOG_CHANNEL) MessageChannel messageChannel,
                                                                             RFC5424MessageConverter converter) {
        TcpSyslogReceivingChannelAdapter adapter = new TcpSyslogReceivingChannelAdapter();
        adapter.setConnectionFactory(serverConnectionFactory);
        adapter.setConverter(converter);
        adapter.setOutputChannel(messageChannel);

        return adapter;
    }
}
