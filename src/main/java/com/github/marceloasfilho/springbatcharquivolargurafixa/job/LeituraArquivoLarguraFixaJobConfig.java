package com.github.marceloasfilho.springbatcharquivolargurafixa.job;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class LeituraArquivoLarguraFixaJobConfig {

    @Bean
    public Job leituraArquivoLarguraFixaJob(Step leituraArquivoLarguraFixaStep, JobRepository jobRepository) {
        return new JobBuilder("arquivoLeituraFixaJob", jobRepository)
                .start(leituraArquivoLarguraFixaStep)
                .incrementer(new RunIdIncrementer())
                .build();
    }
}
