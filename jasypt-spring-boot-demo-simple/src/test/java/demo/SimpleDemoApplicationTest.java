package demo;

import com.ulisesbocchio.jasyptspringboot.EncryptablePropertyResolver;
import org.jasypt.encryption.StringEncryptor;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.env.RandomValuePropertySource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Objects;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SimpleDemoApplication.class)
public class SimpleDemoApplicationTest {

    @Autowired
    ConfigurableEnvironment environment;

    @Autowired
    MyService service;

    @Autowired
    EncryptablePropertyResolver resolver;

    @Autowired
    StringEncryptor encryptor;

    static {
        System.setProperty("jasypt.encryptor.password", "password");
		System.setProperty("ENCRYPTED_PASSWORD", "9ah+QnEdccHCkARkGZ7f0v5BLXXC+z0mr4hyjgE8T2G7mF75OBU1DgmC0YsGis8x");
    }

    @Test
    public void testStringEncryptorIsPresent() {
        Assert.assertNotNull("StringEncryptor should be present", encryptor);
    }

    @Test
    public void testEnvironmentProperties() {
        Assert.assertEquals("chupacabras", environment.getProperty("secret.property"));
        Assert.assertEquals("chupacabras", environment.getProperty("secret2.property"));
    }

	@Test
	public void testIndirectPropertiesDirectly() {
		Assert.assertEquals("chupacabras", environment.getProperty("indirect.secret.property"));
		Assert.assertEquals("chupacabras", environment.getProperty("indirect.secret.property2"));
		Assert.assertEquals("https://uli:chupacabras@localhost:30000", environment.getProperty("endpoint"));
	}

    @Test
    public void testServiceProperties() {
        Assert.assertEquals("chupacabras", service.getSecret());
        Assert.assertEquals("chupacabras", service.getSecret2());
    }

    @Test
    public void testSkipRandomPropertySource() {
        Assert.assertEquals(Objects.requireNonNull(environment.getPropertySources().get("random")).getClass(), RandomValuePropertySource.class);
    }

}
