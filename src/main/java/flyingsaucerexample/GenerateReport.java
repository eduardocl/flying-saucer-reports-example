package flyingsaucerexample;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FileUtils;
import org.xhtmlrenderer.pdf.ITextRenderer;
import org.xml.sax.SAXException;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;
import com.lowagie.text.DocumentException;

public class GenerateReport {

	public static void main(String[] args)
			throws IOException, ParserConfigurationException, SAXException, DocumentException {

		TemplateLoader loader = new ClassPathTemplateLoader();
		loader.setPrefix("/reports");
		loader.setSuffix(".xhtml");
		Handlebars handlebars = new Handlebars(loader);
		Template template = handlebars.compile("report");

		String context = System.getProperty("user.dir") + "/src/main/resources/reports";

		Map objs = new HashMap<String, Object>();

		List<Product> products = new ArrayList<Product>();

		for (int i = 0; i < 100; i++) {
			Product p = new Product();
			p.setName("Tablet 7''");
			p.setCategory("Eletronics");
			p.setPrice("$ 100.00");
			products.add(p);
		}

		objs.put("context", context);
		objs.put("products", products);

		String relatorioHTML = template.apply(objs);

		FileUtils.writeByteArrayToFile(new File("report.html"), relatorioHTML.getBytes());

		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		ITextRenderer renderer = new ITextRenderer();
		renderer.setDocumentFromString(relatorioHTML);
		renderer.layout();

		renderer.createPDF(baos);

		byte[] result = baos.toByteArray();
		baos.close();

		FileUtils.writeByteArrayToFile(new File("report.pdf"), result);

		System.out.println("Report generated!");

	}

}
