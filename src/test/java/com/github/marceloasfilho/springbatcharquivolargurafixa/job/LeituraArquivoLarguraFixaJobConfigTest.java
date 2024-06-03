package com.github.marceloasfilho.springbatcharquivolargurafixa.job;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBatchTest
@SpringBootTest
public class LeituraArquivoLarguraFixaJobConfigTest {
    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    private JobParameters jobParameters;

    @BeforeEach
    public void setUp() {
        this.jobParameters = new JobParametersBuilder()
                .addString("arquivoClientes", "files/clientes.txt")
                .toJobParameters();
    }

    @Test
    public void deveExecutarJobComSucesso() throws Exception {
        // Ação
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(this.jobParameters);

        // Verificação
        assertEquals("COMPLETED", jobExecution.getExitStatus().getExitCode());
        assertEquals("arquivoLeituraFixaJob", jobExecution.getJobInstance().getJobName());
    }
}
