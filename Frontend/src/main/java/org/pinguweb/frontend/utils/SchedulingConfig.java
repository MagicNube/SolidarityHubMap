package org.pinguweb.frontend.utils;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling  // Activa procesamiento de @Scheduled
public class SchedulingConfig { }