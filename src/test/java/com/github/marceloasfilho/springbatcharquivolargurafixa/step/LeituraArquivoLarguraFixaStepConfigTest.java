package com.github.marceloasfilho.springbatcharquivolargurafixa.step;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
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
@ExtendWith(MockitoExtension.class)
public class LeituraArquivoLarguraFixaStepConfigTest {

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
    public void deveTestarStepComSucesso() {
        // Ação
        JobExecution jobExecution = this.jobLauncherTestUtils.launchStep("leituraArquivoLarguraFixaStep", this.jobParameters);

        // Verificação
        assertEquals(1, jobExecution.getStepExecutions().size());
        assertEquals("COMPLETED", jobExecution.getExitStatus().getExitCode());
    }
}
