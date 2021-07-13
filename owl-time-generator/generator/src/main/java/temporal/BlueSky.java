package temporal;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.RDFXMLDocumentFormat;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.TimePeriod;

import java.io.File;
import java.io.FileOutputStream;

public class BlueSky {

    private static final String origDir = "C:\\Users\\gaziz\\Desktop\\bundle\\orig\\";
    private static final String outputDir = "C:\\Users\\gaziz\\Desktop\\bundle\\timeDS\\";

    public static void main(String[] args) throws Exception {
        File dir = new File(origDir);
        for (File file : dir.listFiles()) {
            processOntology(file);
        }
    }

    public static void processOntology(File file) throws Exception {
        OWLOntologyManager ontologyManager = OWLManager.createOWLOntologyManager();
        OWLOntology ontology = ontologyManager.loadOntologyFromOntologyDocument(file);
        for (OWLObjectPropertyAssertionAxiom axiom : ontology.getAxioms(AxiomType.OBJECT_PROPERTY_ASSERTION)) {
            TimePeriod timePeriod = TimePeriodUtil.random(0L, 1000L);
            axiom.addPeriod(timePeriod);
        }
        ontologyManager.saveOntology(ontology, new RDFXMLDocumentFormat(), new FileOutputStream(outputDir + file.getName()));
    }
}