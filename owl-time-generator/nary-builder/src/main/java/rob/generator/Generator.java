package rob.generator;

import java.io.File;
import java.io.FileOutputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.RDFXMLDocumentFormat;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.TimePeriod;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;
import time.TimeFactory;

public class Generator {

   private static final String inputDir = "C:\\Users\\gaziz\\Desktop\\bundle\\timeDS\\";
   private static final File timeFile = new File("src\\main\\resources\\timeEvents.owl");
   private static final String outputDir = "C:\\Users\\gaziz\\Desktop\\bundle\\owl-time\\";

   private static OWLOntologyManager owlOntologyManager;
   private static OWLOntology ontology;
   private static final StructuralReasonerFactory structuralReasonerFactory = new StructuralReasonerFactory();

   public static void main(String[] args) throws Exception {
      File dir = new File(inputDir);
      for (File file : dir.listFiles()) {
         owlOntologyManager = OWLManager.createOWLOntologyManager();
         ontology = owlOntologyManager.loadOntologyFromOntologyDocument(file);
//         OWLReasoner reasoner = PelletReasonerFactory.getInstance().createReasoner(ontology);
         OWLReasoner reasoner = structuralReasonerFactory.createReasoner(ontology);
         Set<OWLObjectProperty> convertedProperties = new HashSet<>();
         long assertionsConverted = 0;

         mergeWithOWLTime();

         TimeFactory.initialize(owlOntologyManager, ontology, reasoner);
         TimeFactory.createRequiredEntities();

         for (OWLObjectPropertyAssertionAxiom axiom : ontology.getAxioms(AxiomType.OBJECT_PROPERTY_ASSERTION)) {
            if (axiom.getPeriods() != null) {
               for (TimePeriod period : axiom.getPeriods()) {
                  OWLNamedIndividual interval = TimeFactory.createInterval(
                          period.getStart().toString(), period.getEnd().toString(),
                          null, null
                  );

                  OWLObjectProperty property = axiom.getProperty().asOWLObjectProperty();

                  if (!convertedProperties.contains(property)) {
                     TimeFactory.convertObjectPropertyToTemporal(property);
                     convertedProperties.add(property);
                  }

                  TimeFactory.convertObjectPropertyAssertion(axiom, interval);
                  assertionsConverted += 1;
               }
            }
         }

         owlOntologyManager.saveOntology(ontology, new RDFXMLDocumentFormat(), new FileOutputStream(outputDir + file.getName()));

         System.out.println(MessageFormat.format(
                 "{0} | CONVERTED TO TEMPORAL: {1} properties, {2} property assertions.",
                 file.getName(), convertedProperties.size(), assertionsConverted
         ));
      }
   }



   private static void mergeWithOWLTime() throws Exception {
      OWLOntology timeOnt = owlOntologyManager.loadOntologyFromOntologyDocument(timeFile);
      ArrayList<AddAxiom> axiomsToAdd = new ArrayList<AddAxiom>();

      for (OWLOntology ont : owlOntologyManager.getOntologies()) {
         for (OWLAxiom axiom : ont.getAxioms()) {
            axiomsToAdd.add(new AddAxiom(ontology, axiom));
         }
      }

      owlOntologyManager.applyChanges(axiomsToAdd);
      owlOntologyManager.removeOntology(timeOnt);
   }

}
