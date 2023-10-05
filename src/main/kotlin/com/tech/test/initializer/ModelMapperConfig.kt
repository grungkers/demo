package com.tech.test.initializer

import org.modelmapper.ModelMapper
import org.modelmapper.config.Configuration
import org.springframework.context.annotation.Bean


@org.springframework.context.annotation.Configuration
class ModelMapperConfig {
    @Bean
    fun modelMapper(): ModelMapper {
        val modelMapper = ModelMapper()
        modelMapper.getConfiguration()
            .setFieldMatchingEnabled(true)
            .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE)
        return modelMapper
    }
}