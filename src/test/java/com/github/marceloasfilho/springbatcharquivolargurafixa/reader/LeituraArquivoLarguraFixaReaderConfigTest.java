package com.github.marceloasfilho.springbatcharquivolargurafixa.reader;

import com.github.marceloasfilho.springbatcharquivolargurafixa.entity.Cliente;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.batch.test.MetaDataInstanceFactory;
import org.springframework.batch.test.StepScopeTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(properties = "spring.batch.job.enabled=false")
public class LeituraArquivoLarguraFixaReaderConfigTest {

    @Autowired
    private LeituraArquivoLarguraFixaReaderConfig leituraArquivoLarguraFixaReaderConfig;

    @Value("files/clientes.txt")
    private ClassPathResource arquivoCorreto;

    @Value("files/clientes_delimitador_incorreto.txt")
    private ClassPathResource arquivoComDelimitadorIncorreto;

    @Value("files/clientes_campos_faltantes.txt")
    private ClassPathResource arquivoComCamposFaltantes;

    @Value("files/clientes_campos_extras.txt")
    private ClassPathResource arquivoComCamposExtras;

    private StepExecution createStepExecution(ClassPathResource resource) {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("arquivoClientes", resource.getPath())
                .toJobParameters();
        return MetaDataInstanceFactory.createStepExecution(jobParameters);
    }

    @Test
    public void deveTestarReaderAoLerArquivoClientesComLarguraFixaComSucesso() throws Exception {
        // Cenário
        StepExecution stepExecution = this.createStepExecution(this.arquivoCorreto);

        // Ação
        StepScopeTestUtils.doInStepScope(stepExecution, () -> {
            FlatFileItemReader<Cliente> reader = leituraArquivoLarguraFixaReaderConfig.leituraArquivoLarguraFixaReader(this.arquivoCorreto);
            reader.open(stepExecution.getExecutionContext());

            Cliente cliente1 = reader.read();
            Cliente cliente2 = reader.read();
            Cliente cliente3 = reader.read();

            // Verificação
            assertNotNull(cliente1);
            assertEquals("João", cliente1.getNome().trim());
            assertEquals("Silva", cliente1.getSobrenome().trim());
            assertEquals("32", cliente1.getIdade());
            assertEquals("joao@yahoo.com", cliente1.getEmail());

            assertNotNull(cliente2);
            assertEquals("Maria", cliente2.getNome().trim());
            assertEquals("Cecilia", cliente2.getSobrenome().trim());
            assertEquals("32", cliente2.getIdade());
            assertEquals("maria@gmail.com", cliente2.getEmail());

            assertNotNull(cliente3);
            assertEquals("José", cliente3.getNome().trim());
            assertEquals("Silva", cliente3.getSobrenome().trim());
            assertEquals("32", cliente3.getIdade());
            assertEquals("jose@outlook.com", cliente3.getEmail());

            reader.close();
            return null;
        });
    }

    @Test
    public void deveTestarReaderAoLerArquivoClientesComLarguraFixaComDelimitadorIncorretoOuCamposExtras() throws Exception {

        ClassPathResource[] arquivosComFalhas = {this.arquivoComDelimitadorIncorreto, this.arquivoComCamposExtras};

        for (ClassPathResource arquivoComFalha : arquivosComFalhas) {

            // Cenário
            StepExecution stepExecution = this.createStepExecution(arquivoComFalha);

            // Ação
            StepScopeTestUtils.doInStepScope(stepExecution, () -> {
                FlatFileItemReader<Cliente> reader = leituraArquivoLarguraFixaReaderConfig.leituraArquivoLarguraFixaReader(arquivoComFalha);
                reader.open(stepExecution.getExecutionContext());

                // Verificação
                Cliente cliente1 = reader.read();
                assertNotNull(cliente1);
                assertNotEquals("João", cliente1.getNome().trim());
                assertNotEquals("Silva", cliente1.getSobrenome().trim());
                assertNotEquals("32", cliente1.getIdade());
                assertNotEquals("joao@yahoo.com", cliente1.getEmail());

                Cliente cliente2 = reader.read();
                assertNotNull(cliente2);
                assertNotEquals("Maria", cliente2.getNome().trim());
                assertNotEquals("Cecilia", cliente2.getSobrenome().trim());
                assertNotEquals("32", cliente2.getIdade());
                assertNotEquals("maria@gmail.com", cliente2.getEmail());

                Cliente cliente3 = reader.read();
                assertNotNull(cliente3);
                assertNotEquals("José", cliente3.getNome().trim());
                assertNotEquals("Silva", cliente3.getSobrenome().trim());
                assertNotEquals("32", cliente3.getIdade());
                assertNotEquals("jose@outlook.com", cliente3.getEmail());

                reader.close();
                return null;
            });
        }
    }

    @Test
    public void deveLancarFlatFileParseExceptionAoLerArquivoClientesComLarguraFixaComCamposFaltantes() throws Exception {
        // Cenário
        StepExecution stepExecution = this.createStepExecution(this.arquivoComCamposFaltantes);

        // Ação
        StepScopeTestUtils.doInStepScope(stepExecution, () -> {
            FlatFileItemReader<Cliente> reader = leituraArquivoLarguraFixaReaderConfig.leituraArquivoLarguraFixaReader(this.arquivoComCamposFaltantes);
            reader.open(stepExecution.getExecutionContext());

            // Verificação
            assertThrows(FlatFileParseException.class, reader::read);

            reader.close();
            return null;
        });
    }
}
