package com.github.marceloasfilho.springbatcharquivolargurafixa.job;

import org.junit.jupiter.api.Test;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBatchTest
@SpringBootTest
public class LeituraArquivoLarguraFixaJobConfigTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Value("files/clientes.txt")
    private ClassPathResource arquivoCorreto;

    @Value("files/clientes_delimitador_incorreto.txt")
    private ClassPathResource arquivoComDelimitadorIncorreto;

    @Value("files/clientes_campos_faltantes.txt")
    private ClassPathResource arquivoComCamposFaltantes;

    @Value("files/clientes_campos_extras.txt")
    private ClassPathResource arquivoComCamposExtras;

    private JobParameters createJobParameters(ClassPathResource resource) {
        return new JobParametersBuilder()
                .addString("arquivoClientes", resource.getPath())
                .toJobParameters();
    }

    @Test
    public void deveExecutarJobComSucessoAoReceberArquivoDeClientesCorreto() throws Exception {
        // Ação
        JobExecution jobExecution = this.jobLauncherTestUtils.launchJob(this.createJobParameters(this.arquivoCorreto));

        // Verificação
        assertEquals("COMPLETED", jobExecution.getExitStatus().getExitCode());
        assertEquals("arquivoLeituraFixaJob", jobExecution.getJobInstance().getJobName());
    }

    @Test
    public void deveExecutarJobComStatusCompletedAoReceberArquivosDeClientesComDelimitadorIncorretoOuCamposExtras() throws Exception {
        // Ação
        ClassPathResource[] arquivosComFalhas = {this.arquivoComDelimitadorIncorreto, this.arquivoComCamposExtras};

        for (ClassPathResource arquivoComFalha : arquivosComFalhas) {
            JobExecution jobExecution = this.jobLauncherTestUtils.launchJob(this.createJobParameters(arquivoComFalha));

            // Verificação
            assertEquals("COMPLETED", jobExecution.getExitStatus().getExitCode());
            assertEquals("arquivoLeituraFixaJob", jobExecution.getJobInstance().getJobName());
        }
    }

    @Test
    public void deveExecutarJobComStatusFailedAoReceberArquivosDeClientesComCamposFaltantes() throws Exception {
        // Ação
        JobExecution jobExecution = this.jobLauncherTestUtils.launchJob(this.createJobParameters(this.arquivoComCamposFaltantes));

        // Verificação
        assertEquals("FAILED", jobExecution.getExitStatus().getExitCode());
        assertEquals("arquivoLeituraFixaJob", jobExecution.getJobInstance().getJobName());
    }
}
