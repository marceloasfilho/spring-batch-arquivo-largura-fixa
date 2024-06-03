package com.github.marceloasfilho.springbatcharquivolargurafixa.reader;

import com.github.marceloasfilho.springbatcharquivolargurafixa.entity.Cliente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.test.MetaDataInstanceFactory;
import org.springframework.batch.test.StepScopeTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.MockitoAnnotations.openMocks;

@SpringBootTest
@TestPropertySource(properties = "spring.batch.job.enabled=false")
public class LeituraArquivoLarguraFixaReaderConfigTest {
    @Autowired
    private LeituraArquivoLarguraFixaReaderConfig leituraArquivoLarguraFixaReaderConfig;

    private StepExecution stepExecution;

    @BeforeEach
    public void setUp() {
        openMocks(this);
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("arquivoClientes", "files/clientes.txt")
                .toJobParameters();
        this.stepExecution = MetaDataInstanceFactory.createStepExecution(jobParameters);
    }

    @Test
    public void deveLerArquivoClientesComLarguraFixaComSucesso() throws Exception {
        // Cenário
        Resource arquivoClientes = new ClassPathResource("files/clientes.txt");
        FlatFileItemReader<Cliente> reader = leituraArquivoLarguraFixaReaderConfig.leituraArquivoLarguraFixaReader(arquivoClientes);

        // Ação
        StepScopeTestUtils.doInStepScope(this.stepExecution, () -> {
            reader.open(MetaDataInstanceFactory.createStepExecution().getExecutionContext());

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
            return null;
        });
    }
}
