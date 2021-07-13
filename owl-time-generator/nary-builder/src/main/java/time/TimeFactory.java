package time;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;

import com.clarkparsia.pellet.owlapiv3.PelletReasoner;
import org.apache.jena.atlas.lib.ArrayUtils;
import org.protege.editor.owl.model.OWLModelManager;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.ClassExpressionType;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataAllValuesFrom;
import org.semanticweb.owlapi.model.OWLDataExactCardinality;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataHasValue;
import org.semanticweb.owlapi.model.OWLDataMaxCardinality;
import org.semanticweb.owlapi.model.OWLDataMinCardinality;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLDataPropertyExpression;
import org.semanticweb.owlapi.model.OWLDataRange;
import org.semanticweb.owlapi.model.OWLDataSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLDeclarationAxiom;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLNegativeDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLNegativeObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectAllValuesFrom;
import org.semanticweb.owlapi.model.OWLObjectExactCardinality;
import org.semanticweb.owlapi.model.OWLObjectHasValue;
import org.semanticweb.owlapi.model.OWLObjectMaxCardinality;
import org.semanticweb.owlapi.model.OWLObjectMinCardinality;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLObjectPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLObjectUnionOf;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.model.OWLSubObjectPropertyOfAxiom;
import org.semanticweb.owlapi.model.PrefixManager;
import org.semanticweb.owlapi.model.SWRLRule;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.impl.OWLClassNodeSet;
import org.semanticweb.owlapi.util.DefaultPrefixManager;
import org.semanticweb.owlapi.vocab.OWL2Datatype;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;
import structures.ConstraintStructure;
import structures.DataAllValuesFromStructure;
import structures.DataExactCardinalityStructure;
import structures.DataHasValueStructure;
import structures.DataMaxCardinalityStructure;
import structures.DataMinCardinalityStructure;
import structures.DataSomeValuesFromStructure;
import structures.ObjectAllValuesFromStructure;
import structures.ObjectExactCardinalityStructure;
import structures.ObjectHasValueStructure;
import structures.ObjectMaxCardinalityStructure;
import structures.ObjectMinCardinalityStructure;
import structures.ObjectSomeValuesFromStructure;
import structures.TemporalDataPropertyTriple;
import structures.TemporalNegativeDataPropertyTriple;
import structures.TemporalNegativeObjectPropertyTriple;
import structures.TemporalObjectPropertyTriple;
import structures.TemporalPropertyTriple;
import ui.Renderer;

public class TimeFactory {
   private static OWLModelManager modelManager;
   private static OWLReasoner reasoner;
   private static OWLOntologyManager manager;
   private static OWLDataFactory factory;
   private static OWLOntology ontology;
   private static PrefixManager pm;
   private static PrefixManager tpm;
   private static PrefixManager intellipm;
   private static OWLClass event;
   private static OWLObjectProperty during;
   private static OWLObjectProperty dataPropertyOP;
   private static OWLObjectProperty overlaps;
   private static OWLObjectProperty participatesIn;
   private static long lastId;

   public static boolean containsOWLTime(OWLOntology var0) {
      Iterator var1 = var0.getClassesInSignature().iterator();

      OWLClass var2;
      do {
         if (!var1.hasNext()) {
            return false;
         }

         var2 = (OWLClass)var1.next();
      } while(var2.isAnonymous() || !var2.getIRI().toString().contains("http://www.w3.org/2006/time#"));

      return true;
   }

   public static void initialize(OWLOntologyManager owlOntologyManager, OWLOntology owlOntology, OWLReasoner owlReasoner) {
      ontology = owlOntology;
      manager = owlOntologyManager;
      reasoner  = owlReasoner;
      factory = manager.getOWLDataFactory();
      pm = new DefaultPrefixManager(ontology.getOntologyID().getOntologyIRI().get().toString() + "#");
      tpm = new DefaultPrefixManager("http://www.w3.org/2006/time#");
      intellipm = new DefaultPrefixManager("http://www.intelligence.tuc.gr/2011/timeEvents#");
      event = factory.getOWLClass(":Event", intellipm);
      during = factory.getOWLObjectProperty(":during", intellipm);
      dataPropertyOP = factory.getOWLObjectProperty(":dataPropertyOP", intellipm);
      overlaps = factory.getOWLObjectProperty(":overlaps", intellipm);
      participatesIn = factory.getOWLObjectProperty(":participatesIn", intellipm);
      ConstraintFactory.initialize(manager, ontology, reasoner);
   }

   public static void createRequiredEntities() {
      OWLDeclarationAxiom var0 = factory.getOWLDeclarationAxiom(event);
      manager.addAxiom(ontology, var0);
      OWLSubClassOfAxiom var1 = factory.getOWLSubClassOfAxiom(event, factory.getOWLThing());
      manager.addAxiom(ontology, var1);
      OWLAnnotation var2 = factory.getOWLAnnotation(factory.getOWLAnnotationProperty(OWLRDFVocabulary.RDFS_COMMENT.getIRI()), factory.getOWLLiteral("A class that represents a n-ary temporal relation. An instance of the relation linking the n individuals (subject, object, time interval) is then an instance of this class.", "en"));
      OWLAnnotationAssertionAxiom var3 = factory.getOWLAnnotationAssertionAxiom(event.getIRI(), var2);
      manager.addAxiom(ontology, var3);
      OWLDeclarationAxiom var4 = factory.getOWLDeclarationAxiom(during);
      manager.addAxiom(ontology, var4);
      OWLSubObjectPropertyOfAxiom var5 = factory.getOWLSubObjectPropertyOfAxiom(during, factory.getOWLTopObjectProperty());
      manager.addAxiom(ontology, var5);
      OWLObjectPropertyDomainAxiom var6 = factory.getOWLObjectPropertyDomainAxiom(during, event);
      manager.addAxiom(ontology, var6);
      OWLObjectPropertyRangeAxiom var7 = factory.getOWLObjectPropertyRangeAxiom(during, factory.getOWLClass(":Interval", tpm));
      manager.addAxiom(ontology, var7);
      OWLAnnotation var8 = factory.getOWLAnnotation(factory.getOWLAnnotationProperty(OWLRDFVocabulary.RDFS_COMMENT.getIRI()), factory.getOWLLiteral("An object property that relates an instance of the event class to a time interval individual. This means that the current event took place during the specified time interval.", "en"));
      OWLAnnotationAssertionAxiom var9 = factory.getOWLAnnotationAssertionAxiom(during.getIRI(), var8);
      manager.addAxiom(ontology, var9);
      OWLDeclarationAxiom var10 = factory.getOWLDeclarationAxiom(participatesIn);
      manager.addAxiom(ontology, var10);
      OWLObjectPropertyDomainAxiom var11 = factory.getOWLObjectPropertyDomainAxiom(participatesIn, factory.getOWLThing());
      manager.addAxiom(ontology, var11);
      OWLObjectPropertyRangeAxiom var12 = factory.getOWLObjectPropertyRangeAxiom(participatesIn, factory.getOWLThing());
      manager.addAxiom(ontology, var12);
      OWLAnnotation var13 = factory.getOWLAnnotation(factory.getOWLAnnotationProperty(OWLRDFVocabulary.RDFS_COMMENT.getIRI()), factory.getOWLLiteral("Super property of all properties that participate in temporal \"triples\". Every property that is converted to temporal becomes subproperty of this object property.", "en"));
      OWLAnnotationAssertionAxiom var14 = factory.getOWLAnnotationAssertionAxiom(participatesIn.getIRI(), var13);
      manager.addAxiom(ontology, var14);
      OWLDeclarationAxiom var15 = factory.getOWLDeclarationAxiom(dataPropertyOP);
      manager.addAxiom(ontology, var15);
      OWLObjectPropertyDomainAxiom var16 = factory.getOWLObjectPropertyDomainAxiom(dataPropertyOP, factory.getOWLThing());
      manager.addAxiom(ontology, var16);
      OWLObjectPropertyRangeAxiom var17 = factory.getOWLObjectPropertyRangeAxiom(dataPropertyOP, event);
      manager.addAxiom(ontology, var17);
      OWLSubObjectPropertyOfAxiom var18 = factory.getOWLSubObjectPropertyOfAxiom(dataPropertyOP, participatesIn);
      manager.addAxiom(ontology, var18);
      OWLAnnotation var19 = factory.getOWLAnnotation(factory.getOWLAnnotationProperty(OWLRDFVocabulary.RDFS_COMMENT.getIRI()), factory.getOWLLiteral("Super property of all properties that link an individual to an event instance. The specific event is subject of a data property triple.", "en"));
      OWLAnnotationAssertionAxiom var20 = factory.getOWLAnnotationAssertionAxiom(dataPropertyOP.getIRI(), var19);
      manager.addAxiom(ontology, var20);
      OWLDeclarationAxiom var21 = factory.getOWLDeclarationAxiom(overlaps);
      manager.addAxiom(ontology, var21);
      OWLAnnotation var22 = factory.getOWLAnnotation(factory.getOWLAnnotationProperty(OWLRDFVocabulary.RDFS_COMMENT.getIRI()), factory.getOWLLiteral("An object property that relates two time interval instances that some way overlap each other.", "en"));
      OWLAnnotationAssertionAxiom var23 = factory.getOWLAnnotationAssertionAxiom(overlaps.getIRI(), var22);
      manager.addAxiom(ontology, var23);
      OWLSubObjectPropertyOfAxiom var24 = factory.getOWLSubObjectPropertyOfAxiom(factory.getOWLObjectProperty(":intervalContains", tpm), overlaps);
      manager.addAxiom(ontology, var24);
      OWLSubObjectPropertyOfAxiom var25 = factory.getOWLSubObjectPropertyOfAxiom(factory.getOWLObjectProperty(":intervalDuring", tpm), overlaps);
      manager.addAxiom(ontology, var25);
      OWLSubObjectPropertyOfAxiom var26 = factory.getOWLSubObjectPropertyOfAxiom(factory.getOWLObjectProperty(":intervalEquals", tpm), overlaps);
      manager.addAxiom(ontology, var26);
      OWLSubObjectPropertyOfAxiom var27 = factory.getOWLSubObjectPropertyOfAxiom(factory.getOWLObjectProperty(":intervalFinishedBy", tpm), overlaps);
      manager.addAxiom(ontology, var27);
      OWLSubObjectPropertyOfAxiom var28 = factory.getOWLSubObjectPropertyOfAxiom(factory.getOWLObjectProperty(":intervalFinishes", tpm), overlaps);
      manager.addAxiom(ontology, var28);
      OWLSubObjectPropertyOfAxiom var29 = factory.getOWLSubObjectPropertyOfAxiom(factory.getOWLObjectProperty(":intervalOverlappedBy", tpm), overlaps);
      manager.addAxiom(ontology, var29);
      OWLSubObjectPropertyOfAxiom var30 = factory.getOWLSubObjectPropertyOfAxiom(factory.getOWLObjectProperty(":intervalOverlaps", tpm), overlaps);
      manager.addAxiom(ontology, var30);
      OWLSubObjectPropertyOfAxiom var31 = factory.getOWLSubObjectPropertyOfAxiom(factory.getOWLObjectProperty(":intervalStartedBy", tpm), overlaps);
      manager.addAxiom(ontology, var31);
      OWLSubObjectPropertyOfAxiom var32 = factory.getOWLSubObjectPropertyOfAxiom(factory.getOWLObjectProperty(":intervalStarts", tpm), overlaps);
      manager.addAxiom(ontology, var32);
   }

   public static Set getRequiredEntities() {
      HashSet var0 = new HashSet();
      Iterator var1 = getSetOfEventClasses().iterator();

      while(var1.hasNext()) {
         OWLClassExpression var2 = (OWLClassExpression)var1.next();
         if (!var2.isAnonymous()) {
            var0.add(var2.asOWLClass());
         }
      }

      var0.add(dataPropertyOP);
      var1 = reasoner.getSubObjectProperties(dataPropertyOP, true).iterator();

      while(var1.hasNext()) {
         OWLObjectPropertyExpression var3 = (OWLObjectPropertyExpression)var1.next();
         var0.add(var3.asOWLObjectProperty());
      }

      var0.add(during);
      var0.add(overlaps);
      var0.add(participatesIn);
      return var0;
   }

   public static Set getOWLTimeEntities() {
      HashSet var0 = new HashSet();
      Iterator var1 = ontology.getSignature().iterator();

      while(var1.hasNext()) {
         OWLEntity var2 = (OWLEntity)var1.next();
         if (var2.getIRI().toString().contains(tpm.getDefaultPrefix())) {
            var0.add(var2);
         }
      }

      return var0;
   }

   public static boolean isTemporalProperty(OWLEntity var0) {
      Set var1 = getSetOfEventClasses();
      boolean var2 = false;
      boolean var3 = false;
      Iterator var5;
      OWLClassExpression var6;
      Set var7;
      Iterator var8;
      OWLClassExpression var9;
      if (var0.isOWLObjectProperty()) {
         OWLObjectProperty var10 = var0.asOWLObjectProperty();
         var5 = reasoner.getObjectPropertyDomains(var10, true).iterator();

         while(true) {
            while(true) {
               do {
                  if (!var5.hasNext()) {
                     var5 = reasoner.getObjectPropertyRanges(var10, true).iterator();

                     while(true) {
                        while(true) {
                           do {
                              if (!var5.hasNext()) {
                                 return var2 && var3;
                              }

                              var6 = (OWLClassExpression)var5.next();
                              var7 = var6.asDisjunctSet();
                           } while(var7.size() <= 1);

                           var8 = var7.iterator();

                           while(var8.hasNext()) {
                              var9 = (OWLClassExpression)var8.next();
                              if (var1.contains(var9)) {
                                 var3 = true;
                                 break;
                              }
                           }
                        }
                     }
                  }

                  var6 = (OWLClassExpression)var5.next();
                  var7 = var6.asDisjunctSet();
               } while(var7.size() <= 1);

               var8 = var7.iterator();

               while(var8.hasNext()) {
                  var9 = (OWLClassExpression)var8.next();
                  if (var1.contains(var9)) {
                     var2 = true;
                     break;
                  }
               }
            }
         }
      } else if (!var0.isOWLDataProperty()) {
         return false;
      } else {
         OWLDataProperty var4 = var0.asOWLDataProperty();
         var5 = reasoner.getDataPropertyDomains(var4, true).iterator();

         while(true) {
            while(true) {
               do {
                  if (!var5.hasNext()) {
                     return var2;
                  }

                  var6 = (OWLClassExpression)var5.next();
                  var7 = var6.asDisjunctSet();
               } while(var7.size() <= 1);

               var8 = var7.iterator();

               while(var8.hasNext()) {
                  var9 = (OWLClassExpression)var8.next();
                  if (var1.contains(var9)) {
                     var2 = true;
                     break;
                  }
               }
            }
         }
      }
   }

   public static void convertObjectPropertyToTemporal(OWLObjectProperty staticObProp, OWLNamedIndividual interval) {
      Set var2 = (Set) reasoner.getObjectPropertyDomains(staticObProp, true);
      Set var3 = (Set) reasoner.getObjectPropertyRanges(staticObProp, true);
      if (var2.isEmpty()) {
         var2.add(factory.getOWLThing());
      }

      if (var3.isEmpty()) {
         var3.add(factory.getOWLThing());
      }

      Iterator var4 = var2.iterator();

      OWLClassExpression var5;
      while(var4.hasNext()) {
         var5 = (OWLClassExpression)var4.next();
         OWLObjectPropertyDomainAxiom var6 = factory.getOWLObjectPropertyDomainAxiom(staticObProp, var5);
         manager.removeAxiom(ontology, var6);
      }

      var4 = var3.iterator();

      while(var4.hasNext()) {
         var5 = (OWLClassExpression)var4.next();
         OWLObjectPropertyRangeAxiom var10 = factory.getOWLObjectPropertyRangeAxiom(staticObProp, var5);
         manager.removeAxiom(ontology, var10);
      }

      var4 = var2.iterator();

      OWLObjectUnionOf var7;
      HashSet var11;
      while(var4.hasNext()) {
         var5 = (OWLClassExpression)var4.next();
         var11 = new HashSet();
         var11.add(var5);
         var11.add(event);
         var7 = factory.getOWLObjectUnionOf(var11);
         OWLObjectPropertyDomainAxiom var8 = factory.getOWLObjectPropertyDomainAxiom(staticObProp, var7);
         manager.addAxiom(ontology, var8);
      }

      var4 = var3.iterator();

      while(var4.hasNext()) {
         var5 = (OWLClassExpression)var4.next();
         var11 = new HashSet();
         var11.add(var5);
         var11.add(event);
         var7 = factory.getOWLObjectUnionOf(var11);
         OWLObjectPropertyRangeAxiom var12 = factory.getOWLObjectPropertyRangeAxiom(staticObProp, var7);
         manager.addAxiom(ontology, var12);
      }

      OWLSubObjectPropertyOfAxiom var9 = factory.getOWLSubObjectPropertyOfAxiom(staticObProp, participatesIn);
      manager.addAxiom(ontology, var9);
      convertObjectPropertyTriples(staticObProp, interval);
      applyConstraintsOnObjectProperty(staticObProp);
//      modelManager.setDirty(ontology);
   }

   public static void convertObjectPropertyToTemporal(OWLObjectProperty staticObProp) {
      Set<OWLClassExpression> domains = ontology.getObjectPropertyDomainAxioms(staticObProp).stream().map(OWLPropertyDomainAxiom::getDomain).collect(Collectors.toSet());
      Set<OWLClassExpression> ranges  = ontology.getObjectPropertyRangeAxioms(staticObProp).stream().map(OWLObjectPropertyRangeAxiom::getRange).collect(Collectors.toSet());
      if (domains.isEmpty()) {
         domains.add(factory.getOWLThing());
      }
      if (ranges.isEmpty()) {
         ranges.add(factory.getOWLThing());
      }
      for (OWLClassExpression domain : domains) {
         OWLObjectPropertyDomainAxiom ax = factory.getOWLObjectPropertyDomainAxiom(staticObProp, domain);
         manager.removeAxiom(ontology, ax);
      }
      for (OWLClassExpression range : ranges) {
         OWLObjectPropertyRangeAxiom ax = factory.getOWLObjectPropertyRangeAxiom(staticObProp, range);
         manager.removeAxiom(ontology, ax);
      }
      for (OWLClassExpression domain : domains) {
         OWLObjectUnionOf union = factory.getOWLObjectUnionOf(domain, event);
         OWLObjectPropertyDomainAxiom ax = factory.getOWLObjectPropertyDomainAxiom(staticObProp, union);
         manager.addAxiom(ontology, ax);
      }
      for (OWLClassExpression range : ranges) {
         OWLObjectUnionOf union = factory.getOWLObjectUnionOf(range, event);
         OWLObjectPropertyRangeAxiom ax = factory.getOWLObjectPropertyRangeAxiom(staticObProp, union);
         manager.addAxiom(ontology, ax);
      }

      OWLSubObjectPropertyOfAxiom ax = factory.getOWLSubObjectPropertyOfAxiom(staticObProp, participatesIn);
      manager.addAxiom(ontology, ax);
      applyConstraintsOnObjectProperty(staticObProp);
   }


   public static void convertObjectPropertyAssertion(OWLObjectPropertyAssertionAxiom axiom, OWLNamedIndividual interval) {
      OWLIndividual sub = axiom.getSubject();
      OWLIndividual ob = (OWLIndividual)axiom.getObject();
      manager.removeAxiom(ontology, axiom);
      OWLNamedIndividual eventObject = factory.getOWLNamedIndividual(":Ev" + id(), pm);
      OWLClassAssertionAxiom ass = factory.getOWLClassAssertionAxiom(event, eventObject);
      manager.addAxiom(ontology, ass);
      OWLObjectPropertyAssertionAxiom ax1 = factory.getOWLObjectPropertyAssertionAxiom(
              axiom.getProperty().asOWLObjectProperty(), sub, eventObject
      );
      manager.addAxiom(ontology, ax1);
      OWLObjectPropertyAssertionAxiom ax2 = factory.getOWLObjectPropertyAssertionAxiom(
              axiom.getProperty().asOWLObjectProperty(), eventObject, ob
      );
      manager.addAxiom(ontology, ax2);
      OWLObjectPropertyAssertionAxiom ax3;
      if (interval == null) {
         ax3 = factory.getOWLObjectPropertyAssertionAxiom(during, eventObject, createInterval((String)null, (String)null, (String)null, (OWLNamedIndividual)null));
      } else {
         ax3 = factory.getOWLObjectPropertyAssertionAxiom(during, eventObject, interval);
      }
      manager.addAxiom(ontology, ax3);
   }

   private static void convertObjectPropertyTriples(OWLObjectProperty var0, OWLNamedIndividual var1) {
      Set var2 = getObjectPropertyTriplesContaining(var0);

      OWLObjectPropertyAssertionAxiom var11;
      for(Iterator var3 = var2.iterator(); var3.hasNext();) {
         OWLObjectPropertyAssertionAxiom var4 = (OWLObjectPropertyAssertionAxiom)var3.next();
         convertObjectPropertyAssertion(var4, var1);
      }

      Set var12 = getNegativeObjectPropertyTriplesContaining(var0);
      Iterator var13 = var12.iterator();

      while(var13.hasNext()) {
         OWLNegativeObjectPropertyAssertionAxiom var14 = (OWLNegativeObjectPropertyAssertionAxiom)var13.next();
         manager.removeAxiom(ontology, var14);
         TemporalNegativeObjectPropertyTriple var15 = new TemporalNegativeObjectPropertyTriple(var14.getSubject().asOWLNamedIndividual(), ((OWLIndividual)var14.getObject()).asOWLNamedIndividual(), var1, var0);
         SWRLRule var16 = ConstraintFactory.getTemporalNegativeObjectPropertyAssertionRule(var15);
         manager.addAxiom(ontology, var16);
      }

   }

   public static void convertDataPropertyToTemporal(OWLDataProperty var0, OWLNamedIndividual var1) {
      Set var2 = (Set) reasoner.getDataPropertyDomains(var0, true);
      Set var3 = new HashSet();
      if (var2.isEmpty()) {
         var2.add(factory.getOWLThing());
      }

      if (var3.isEmpty()) {
         var3.add(factory.getTopDatatype());
      }

      Iterator var4 = var2.iterator();

      OWLClassExpression var5;
      while(var4.hasNext()) {
         var5 = (OWLClassExpression)var4.next();
         OWLDataPropertyDomainAxiom var6 = factory.getOWLDataPropertyDomainAxiom(var0, var5);
         manager.removeAxiom(ontology, var6);
      }

      var4 = var2.iterator();

      while(var4.hasNext()) {
         var5 = (OWLClassExpression)var4.next();
         HashSet var11 = new HashSet();
         var11.add(var5);
         var11.add(event);
         OWLObjectUnionOf var7 = factory.getOWLObjectUnionOf(var11);
         OWLDataPropertyDomainAxiom var8 = factory.getOWLDataPropertyDomainAxiom(var0, var7);
         manager.addAxiom(ontology, var8);
      }

      OWLObjectProperty var9 = factory.getOWLObjectProperty(":" + Renderer.getName(var0) + "OP", pm);
      OWLDeclarationAxiom var10 = factory.getOWLDeclarationAxiom(var9);
      manager.addAxiom(ontology, var10);
      Iterator var12 = var2.iterator();

      while(var12.hasNext()) {
         OWLClassExpression var14 = (OWLClassExpression)var12.next();
         OWLObjectPropertyDomainAxiom var15 = factory.getOWLObjectPropertyDomainAxiom(var9, var14);
         manager.addAxiom(ontology, var15);
      }

      OWLSubObjectPropertyOfAxiom var13 = factory.getOWLSubObjectPropertyOfAxiom(var9, dataPropertyOP);
      manager.addAxiom(ontology, var13);
      convertDataPropertyTriples(var0, var9, var1);
      applyConstraintsOnDataProperty(var0);
   }

   private static void convertDataPropertyTriples(OWLDataProperty var0, OWLObjectProperty var1, OWLNamedIndividual var2) {
      Set var3 = getDataPropertyTriplesContaining(var0);

      OWLObjectPropertyAssertionAxiom var12;
      for(Iterator var4 = var3.iterator(); var4.hasNext(); manager.addAxiom(ontology, var12)) {
         OWLDataPropertyAssertionAxiom var5 = (OWLDataPropertyAssertionAxiom)var4.next();
         OWLIndividual var6 = var5.getSubject();
         OWLLiteral var7 = (OWLLiteral)var5.getObject();
         manager.removeAxiom(ontology, var5);
         OWLNamedIndividual var8 = factory.getOWLNamedIndividual(":Ev" + id(), pm);
         OWLClassAssertionAxiom var9 = factory.getOWLClassAssertionAxiom(event, var8);
         manager.addAxiom(ontology, var9);
         OWLObjectPropertyAssertionAxiom var10 = factory.getOWLObjectPropertyAssertionAxiom(var1, var6, var8);
         manager.addAxiom(ontology, var10);
         OWLDataPropertyAssertionAxiom var11 = factory.getOWLDataPropertyAssertionAxiom(var0, var8, var7);
         manager.addAxiom(ontology, var11);
         if (var2 == null) {
            var12 = factory.getOWLObjectPropertyAssertionAxiom(during, var8, createInterval((String)null, (String)null, (String)null, (OWLNamedIndividual)null));
         } else {
            var12 = factory.getOWLObjectPropertyAssertionAxiom(during, var8, var2);
         }
      }

      Set var13 = getNegativeDataPropertyTriplesContaining(var0);
      Iterator var14 = var13.iterator();

      while(var14.hasNext()) {
         OWLNegativeDataPropertyAssertionAxiom var15 = (OWLNegativeDataPropertyAssertionAxiom)var14.next();
         manager.removeAxiom(ontology, var15);
         TemporalNegativeDataPropertyTriple var16 = new TemporalNegativeDataPropertyTriple(var15.getSubject().asOWLNamedIndividual(), (OWLLiteral)var15.getObject(), var2, var1, var0);
         SWRLRule var17 = ConstraintFactory.getTemporalNegativeDataPropertyAssertionRule(var16);
         manager.addAxiom(ontology, var17);
      }

   }

   public static Set getObjectPropertyTriplesContaining(OWLObjectProperty var0) {
      Set var1 = ontology.getIndividualsInSignature();
      HashSet var2 = new HashSet();
      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         OWLNamedIndividual var4 = (OWLNamedIndividual)var3.next();
         Set var5 = ontology.getReferencingAxioms(var4);
         Iterator var6 = var5.iterator();

         while(var6.hasNext()) {
            OWLAxiom var7 = (OWLAxiom)var6.next();
            if (var7.isOfType(new AxiomType[]{AxiomType.OBJECT_PROPERTY_ASSERTION})) {
               OWLObjectPropertyAssertionAxiom var8 = (OWLObjectPropertyAssertionAxiom)var7;
               if (!((OWLObjectPropertyExpression)var8.getProperty()).isAnonymous()) {
                  OWLObjectProperty var9 = ((OWLObjectPropertyExpression)var8.getProperty()).asOWLObjectProperty();
                  if (var9.equals(var0)) {
                     var2.add(var8);
                  }
               }
            }
         }
      }

      return var2;
   }

   public static Set getNegativeObjectPropertyTriplesContaining(OWLObjectProperty var0) {
      Set var1 = ontology.getIndividualsInSignature();
      HashSet var2 = new HashSet();
      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         OWLNamedIndividual var4 = (OWLNamedIndividual)var3.next();
         Set var5 = ontology.getReferencingAxioms(var4);
         Iterator var6 = var5.iterator();

         while(var6.hasNext()) {
            OWLAxiom var7 = (OWLAxiom)var6.next();
            if (var7.isOfType(new AxiomType[]{AxiomType.NEGATIVE_OBJECT_PROPERTY_ASSERTION})) {
               OWLNegativeObjectPropertyAssertionAxiom var8 = (OWLNegativeObjectPropertyAssertionAxiom)var7;
               if (!((OWLObjectPropertyExpression)var8.getProperty()).isAnonymous()) {
                  OWLObjectProperty var9 = ((OWLObjectPropertyExpression)var8.getProperty()).asOWLObjectProperty();
                  if (var9.equals(var0)) {
                     var2.add(var8);
                  }
               }
            }
         }
      }

      return var2;
   }

   public static Set getDataPropertyTriplesContaining(OWLDataProperty var0) {
      Set var1 = ontology.getIndividualsInSignature();
      HashSet var2 = new HashSet();
      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         OWLNamedIndividual var4 = (OWLNamedIndividual)var3.next();
         Set var5 = ontology.getReferencingAxioms(var4);
         Iterator var6 = var5.iterator();

         while(var6.hasNext()) {
            OWLAxiom var7 = (OWLAxiom)var6.next();
            if (var7.isOfType(new AxiomType[]{AxiomType.DATA_PROPERTY_ASSERTION})) {
               OWLDataPropertyAssertionAxiom var8 = (OWLDataPropertyAssertionAxiom)var7;
               if (!((OWLDataPropertyExpression)var8.getProperty()).isAnonymous()) {
                  OWLDataProperty var9 = ((OWLDataPropertyExpression)var8.getProperty()).asOWLDataProperty();
                  if (var9.equals(var0)) {
                     var2.add(var8);
                  }
               }
            }
         }
      }

      return var2;
   }

   public static Set getNegativeDataPropertyTriplesContaining(OWLDataProperty var0) {
      Set var1 = ontology.getIndividualsInSignature();
      HashSet var2 = new HashSet();
      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         OWLNamedIndividual var4 = (OWLNamedIndividual)var3.next();
         Set var5 = ontology.getReferencingAxioms(var4);
         Iterator var6 = var5.iterator();

         while(var6.hasNext()) {
            OWLAxiom var7 = (OWLAxiom)var6.next();
            if (var7.isOfType(new AxiomType[]{AxiomType.NEGATIVE_DATA_PROPERTY_ASSERTION})) {
               OWLNegativeDataPropertyAssertionAxiom var8 = (OWLNegativeDataPropertyAssertionAxiom)var7;
               if (!((OWLDataPropertyExpression)var8.getProperty()).isAnonymous()) {
                  OWLDataProperty var9 = ((OWLDataPropertyExpression)var8.getProperty()).asOWLDataProperty();
                  if (var9.equals(var0)) {
                     var2.add(var8);
                  }
               }
            }
         }
      }

      return var2;
   }

   public static Set getSetOfEventClasses() {
      HashSet var0 = new HashSet();
      Stack var1 = new Stack();
      var1.push(event);

      while(true) {
         OWLClassExpression var2;
         Iterator var3;
         OWLClassExpression var4;
         do {
            do {
               if (var1.empty()) {
                  return var0;
               }

               var2 = (OWLClassExpression)var1.pop();
               var0.add(var2);
            } while(var2.isAnonymous());

            var3 = reasoner.getEquivalentClasses(var2).iterator();

            while(var3.hasNext()) {
               var4 = (OWLClassExpression)var3.next();
               if (!var0.contains(var4)) {
                  var1.push(var4);
               }
            }
         } while(var2.asOWLClass().isBottomEntity());

         var3 = reasoner.getSubClasses(var2, true).iterator();

         while(var3.hasNext()) {
            var4 = (OWLClassExpression)var3.next();
            if (!var0.contains(var4)) {
               var1.push(var4);
            }
         }
      }
   }

   public static Set getProperIndividuals() {
      Set var0 = ontology.getIndividualsInSignature();
      HashSet var1 = new HashSet();
      boolean var2 = true;

      for(Iterator var3 = var0.iterator(); var3.hasNext(); var2 = true) {
         OWLNamedIndividual var4 = (OWLNamedIndividual)var3.next();
         Iterator var5 = reasoner.getTypes(var4, true).iterator();

         while(var5.hasNext()) {
            OWLClassExpression var6 = (OWLClassExpression)var5.next();
            if (getSetOfEventClasses().contains(var6)) {
               var2 = false;
            } else if (!var6.isAnonymous() && var6.asOWLClass().getIRI().toString().contains(tpm.getDefaultPrefix())) {
               var2 = false;
            }
         }

         if (var2) {
            var1.add(var4);
         }
      }

      return var1;
   }

   public static LinkedList getOWLTimeQualitativeProperties() {
      LinkedList var0 = new LinkedList();
      var0.add(factory.getOWLObjectProperty(":intervalAfter", tpm));
      var0.add(factory.getOWLObjectProperty(":intervalBefore", tpm));
      var0.add(factory.getOWLObjectProperty(":intervalContains", tpm));
      var0.add(factory.getOWLObjectProperty(":intervalDuring", tpm));
      var0.add(factory.getOWLObjectProperty(":intervalEquals", tpm));
      var0.add(factory.getOWLObjectProperty(":intervalFinishedBy", tpm));
      var0.add(factory.getOWLObjectProperty(":intervalFinishes", tpm));
      var0.add(factory.getOWLObjectProperty(":intervalMeets", tpm));
      var0.add(factory.getOWLObjectProperty(":intervalMetBy", tpm));
      var0.add(factory.getOWLObjectProperty(":intervalOverlappedBy", tpm));
      var0.add(factory.getOWLObjectProperty(":intervalOverlaps", tpm));
      var0.add(factory.getOWLObjectProperty(":intervalStartedBy", tpm));
      var0.add(factory.getOWLObjectProperty(":intervalStarts", tpm));
      return var0;
   }

   public static void createTemporalObjectPropertyAxiom(OWLClassExpression var0, OWLNamedIndividual var1, String var2, OWLObjectProperty var3, OWLNamedIndividual var4, String var5, String var6, String var7, OWLNamedIndividual var8) {
      OWLNamedIndividual var9;
      if (var1 == null) {
         var9 = factory.getOWLNamedIndividual(":" + var2, pm);
      } else {
         var9 = var1;
      }

      OWLClassAssertionAxiom var10 = factory.getOWLClassAssertionAxiom(var0, var9);
      manager.addAxiom(ontology, var10);
      OWLNamedIndividual var11 = factory.getOWLNamedIndividual(":Ev" + id(), pm);
      OWLClassAssertionAxiom var12 = factory.getOWLClassAssertionAxiom(event, var11);
      manager.addAxiom(ontology, var12);
      OWLObjectPropertyAssertionAxiom var13 = factory.getOWLObjectPropertyAssertionAxiom(var3, var9, var11);
      manager.addAxiom(ontology, var13);
      OWLObjectPropertyAssertionAxiom var14 = factory.getOWLObjectPropertyAssertionAxiom(var3, var11, var4);
      manager.addAxiom(ontology, var14);
      OWLNamedIndividual var15 = createInterval(var5, var6, var7, var8);
      OWLObjectPropertyAssertionAxiom var16 = factory.getOWLObjectPropertyAssertionAxiom(during, var11, var15);
      manager.addAxiom(ontology, var16);
//      modelManager.setDirty(ontology);
   }

   public static void createTemporalDataPropertyAxiom(OWLClassExpression var0, OWLNamedIndividual var1, String var2, OWLDataProperty var3, String var4, String var5, String var6, String var7, OWLNamedIndividual var8) {
      OWLNamedIndividual var9;
      if (var1 == null) {
         var9 = factory.getOWLNamedIndividual(":" + var2, pm);
      } else {
         var9 = var1;
      }

      OWLClassAssertionAxiom var10 = factory.getOWLClassAssertionAxiom(var0, var9);
      manager.addAxiom(ontology, var10);
      OWLNamedIndividual var11 = factory.getOWLNamedIndividual(":Ev" + id(), pm);
      OWLClassAssertionAxiom var12 = factory.getOWLClassAssertionAxiom(event, var11);
      manager.addAxiom(ontology, var12);
      OWLObjectProperty var13 = factory.getOWLObjectProperty(":" + Renderer.getName(var3) + "OP", pm);
      OWLSubObjectPropertyOfAxiom var14 = factory.getOWLSubObjectPropertyOfAxiom(var13, dataPropertyOP);
      manager.addAxiom(ontology, var14);
      OWLObjectPropertyAssertionAxiom var15 = factory.getOWLObjectPropertyAssertionAxiom(var13, var9, var11);
      manager.addAxiom(ontology, var15);
      OWLDatatype var16;
      if (ontology.getDataPropertyRangeAxioms(var3).isEmpty()) {
         var16 = factory.getTopDatatype();
      } else {
         var16 = ((OWLDataRange)ontology.getDataPropertyRangeAxioms(var3).iterator().next()).asOWLDatatype();
      }

      OWLLiteral var17 = factory.getOWLLiteral(var4, var16);
      OWLDataPropertyAssertionAxiom var18 = factory.getOWLDataPropertyAssertionAxiom(var3, var11, var17);
      manager.addAxiom(ontology, var18);
      OWLNamedIndividual var19 = createInterval(var5, var6, var7, var8);
      OWLObjectPropertyAssertionAxiom var20 = factory.getOWLObjectPropertyAssertionAxiom(during, var11, var19);
      manager.addAxiom(ontology, var20);
//      modelManager.setDirty(ontology);
   }

   public static void createTemporalNegativeObjectPropertyAxiom(OWLNamedIndividual var0, OWLObjectProperty var1, OWLNamedIndividual var2, String var3, String var4, String var5, OWLNamedIndividual var6) {
      OWLNamedIndividual var7 = createInterval(var3, var4, var5, var6);
      TemporalNegativeObjectPropertyTriple var8 = new TemporalNegativeObjectPropertyTriple(var0, var2, var7, var1);
      SWRLRule var9 = ConstraintFactory.getTemporalNegativeObjectPropertyAssertionRule(var8);
      manager.addAxiom(ontology, var9);
   }

   public static void createTemporalNegativeDataPropertyAxiom(OWLNamedIndividual var0, OWLDataProperty var1, String var2, String var3, String var4, String var5, OWLNamedIndividual var6) {
      OWLObjectProperty var7 = factory.getOWLObjectProperty(":" + Renderer.getName(var1) + "OP", pm);
      OWLDatatype var8;
      if (ontology.getDataPropertyRangeAxioms(var1).isEmpty()) {
         var8 = factory.getTopDatatype();
      } else {
         var8 = ((OWLDataRange)ontology.getDataPropertyRangeAxioms(var1).iterator().next()).asOWLDatatype();
      }

      OWLLiteral var9 = factory.getOWLLiteral(var2, var8);
      OWLNamedIndividual var10 = createInterval(var3, var4, var5, var6);
      TemporalNegativeDataPropertyTriple var11 = new TemporalNegativeDataPropertyTriple(var0, var9, var10, var7, var1);
      SWRLRule var12 = ConstraintFactory.getTemporalNegativeDataPropertyAssertionRule(var11);
      manager.addAxiom(ontology, var12);
//      modelManager.setDirty(ontology);
   }

   public static void changeInterval(TemporalPropertyTriple var0, String var1, String var2, String var3, OWLNamedIndividual var4) {
      OWLNamedIndividual var5 = var0.getEvent();
      OWLNamedIndividual var6 = var0.getInterval();
      OWLObjectPropertyAssertionAxiom var7 = factory.getOWLObjectPropertyAssertionAxiom(during, var5, var6);
      manager.removeAxiom(ontology, var7);
      OWLNamedIndividual var8 = createInterval(var1, var2, var3, var4);
      OWLObjectPropertyAssertionAxiom var9 = factory.getOWLObjectPropertyAssertionAxiom(during, var5, var8);
      manager.addAxiom(ontology, var9);
//      modelManager.setDirty(ontology);
   }

   public static void changeNegativeInterval(TemporalPropertyTriple var0, String var1, String var2, String var3, OWLNamedIndividual var4) {
      SWRLRule var6;
      OWLNamedIndividual var7;
      SWRLRule var8;
      if (var0.isObjectPropertyTriple()) {
         TemporalNegativeObjectPropertyTriple var5 = (TemporalNegativeObjectPropertyTriple)var0;
         var6 = ConstraintFactory.getTemporalNegativeObjectPropertyAssertionRule(var5);
         manager.removeAxiom(ontology, var6);
         var7 = createInterval(var1, var2, var3, var4);
         var5.setInterval(var7);
         var8 = ConstraintFactory.getTemporalNegativeObjectPropertyAssertionRule(var5);
         manager.addAxiom(ontology, var8);
      } else {
         TemporalNegativeDataPropertyTriple var9 = (TemporalNegativeDataPropertyTriple)var0;
         var6 = ConstraintFactory.getTemporalNegativeDataPropertyAssertionRule(var9);
         manager.removeAxiom(ontology, var6);
         var7 = createInterval(var1, var2, var3, var4);
         var9.setInterval(var7);
         var8 = ConstraintFactory.getTemporalNegativeDataPropertyAssertionRule(var9);
         manager.addAxiom(ontology, var8);
      }

//      modelManager.setDirty(ontology);
   }

   public static String[] getIntervalBounds(OWLNamedIndividual var0) {
      String var1 = new String();
      String var2 = new String();
      Iterator var3 = ontology.getObjectPropertyAssertionAxioms(var0).iterator();

      while(var3.hasNext()) {
         OWLObjectPropertyAssertionAxiom var4 = (OWLObjectPropertyAssertionAxiom)var3.next();
         OWLNamedIndividual var5;
         if (((OWLObjectPropertyExpression)var4.getProperty()).asOWLObjectProperty().getIRI().toString().equalsIgnoreCase("http://www.w3.org/2006/time#hasBeginning")) {
            var5 = ((OWLIndividual)var4.getObject()).asOWLNamedIndividual();
            if (ontology.getDataPropertyAssertionAxioms(var5).isEmpty()) {
               var1 = Renderer.getName(var5);
            } else {
               var1 = ((OWLLiteral)((OWLDataPropertyAssertionAxiom)ontology.getDataPropertyAssertionAxioms(var5).iterator().next()).getObject()).getLiteral();
            }
         } else if (((OWLObjectPropertyExpression)var4.getProperty()).asOWLObjectProperty().getIRI().toString().equalsIgnoreCase("http://www.w3.org/2006/time#hasEnd")) {
            var5 = ((OWLIndividual)var4.getObject()).asOWLNamedIndividual();
            if (ontology.getDataPropertyAssertionAxioms(var5).isEmpty()) {
               var2 = Renderer.getName(var5);
            } else {
               var2 = ((OWLLiteral)((OWLDataPropertyAssertionAxiom)ontology.getDataPropertyAssertionAxioms(var5).iterator().next()).getObject()).getLiteral();
            }
         }
      }

      String[] var6 = new String[]{var1, var2};
      return var6;
   }

   public static void removePropertyAssertion(TemporalPropertyTriple var0) {
      if (var0.isTemporal()) {
         if (!var0.isNegative()) {
            manager.removeAxioms(ontology, ontology.getReferencingAxioms(var0.getEvent()));
         } else {
            SWRLRule var1 = var0.isObjectPropertyTriple() ? ConstraintFactory.getTemporalNegativeObjectPropertyAssertionRule((TemporalNegativeObjectPropertyTriple)var0) : ConstraintFactory.getTemporalNegativeDataPropertyAssertionRule((TemporalNegativeDataPropertyTriple)var0);
            manager.removeAxiom(ontology, var1);
         }
      } else if (var0.isObjectPropertyTriple() && !var0.isNegative()) {
         TemporalObjectPropertyTriple var6 = (TemporalObjectPropertyTriple)var0;
         OWLObjectPropertyAssertionAxiom var9 = factory.getOWLObjectPropertyAssertionAxiom(var6.getObjectProperty(), var6.getSubject(), var6.getObject());
         manager.removeAxiom(ontology, var9);
      } else if (var0.isObjectPropertyTriple() && var0.isNegative()) {
         TemporalNegativeObjectPropertyTriple var5 = (TemporalNegativeObjectPropertyTriple)var0;
         OWLNegativeObjectPropertyAssertionAxiom var8 = factory.getOWLNegativeObjectPropertyAssertionAxiom(var5.getObjectProperty(), var5.getSubject(), var5.getObject());
         manager.removeAxiom(ontology, var8);
      } else if (!var0.isObjectPropertyTriple() && !var0.isNegative()) {
         TemporalDataPropertyTriple var4 = (TemporalDataPropertyTriple)var0;
         OWLDataPropertyAssertionAxiom var7 = factory.getOWLDataPropertyAssertionAxiom(var4.getDataProperty(), var4.getSubject(), var4.getObject().getLiteral());
         manager.removeAxiom(ontology, var7);
      } else {
         TemporalNegativeDataPropertyTriple var3 = (TemporalNegativeDataPropertyTriple)var0;
         OWLNegativeDataPropertyAssertionAxiom var2 = factory.getOWLNegativeDataPropertyAssertionAxiom(var3.getDataProperty(), var3.getSubject(), var3.getObject());
         manager.removeAxiom(ontology, var2);
      }

//      modelManager.setDirty(ontology);
   }

   public static void removeSuperClassConstraint(ConstraintStructure var0) {
      if (var0.getConstraintType() == ConstraintStructure.ConstraintType.NOT_CONSTRAINT) {
         OWLSubClassOfAxiom var1 = factory.getOWLSubClassOfAxiom(var0.getDomain(), var0.getConstraint());
         manager.removeAxiom(ontology, var1);
      } else {
         manager.removeAxioms(ontology, ConstraintFactory.getTemporalSuperClassConstraint(var0));
      }

   }

   public static void removeEquivalentClassConstraint(ConstraintStructure var0) {
      if (var0.getConstraintType() == ConstraintStructure.ConstraintType.NOT_CONSTRAINT) {
         OWLEquivalentClassesAxiom var1 = factory.getOWLEquivalentClassesAxiom(var0.getDomain(), var0.getConstraint());
         manager.removeAxiom(ontology, var1);
      } else {
         manager.removeAxioms(ontology, ConstraintFactory.getTemporalEquivalentClassConstraint(var0));
      }

   }

   public static void addSuperClassConstraint(ConstraintStructure var0) {
      Set var1 = ConstraintFactory.getTemporalSuperClassConstraint(var0);
      manager.addAxioms(ontology, var1);
   }

   public static void addEquivalentClassConstraint(ConstraintStructure var0) {
      Set var1 = ConstraintFactory.getTemporalEquivalentClassConstraint(var0);
      manager.addAxioms(ontology, var1);
   }

   public static OWLNamedIndividual createInterval(String start, String end, String qual, OWLNamedIndividual interv) {
      OWLNamedIndividual var4 = null;
      if (qual == null && interv != null) {
         var4 = interv;
      } else {
         OWLClass var5;
         OWLClassAssertionAxiom var6;
         if (start != null && end != null) {
            var4 = factory.getOWLNamedIndividual(":DTI" + id(), pm);
            var5 = factory.getOWLClass(":DateTimeInterval", tpm);
            var6 = factory.getOWLClassAssertionAxiom(var5, var4);
            manager.addAxiom(ontology, var6);
         } else {
            var4 = factory.getOWLNamedIndividual(":UI" + id(), pm);
            var5 = factory.getOWLClass(":ProperInterval", tpm);
            var6 = factory.getOWLClassAssertionAxiom(var5, var4);
            manager.addAxiom(ontology, var6);
         }

         var5 = factory.getOWLClass(":Instant", tpm);
         OWLClassAssertionAxiom var7;
         OWLNamedIndividual var15;
         if (start == null) {
            var15 = factory.getOWLNamedIndividual(":SU" + id(), pm);
            var7 = factory.getOWLClassAssertionAxiom(var5, var15);
            manager.addAxiom(ontology, var7);
         } else {
            var15 = factory.getOWLNamedIndividual(":Ins" + id(), pm);
            var7 = factory.getOWLClassAssertionAxiom(var5, var15);
            manager.addAxiom(ontology, var7);
            OWLLiteral var8 = factory.getOWLLiteral(start, OWL2Datatype.XSD_DATE_TIME);
            OWLDataProperty var9 = factory.getOWLDataProperty(":inXSDDateTime", tpm);
            OWLDataPropertyAssertionAxiom var10 = factory.getOWLDataPropertyAssertionAxiom(var9, var15, var8);
            manager.addAxiom(ontology, var10);
         }

         OWLObjectProperty var16 = factory.getOWLObjectProperty(":hasBeginning", tpm);
         OWLObjectPropertyAssertionAxiom var17 = factory.getOWLObjectPropertyAssertionAxiom(var16, var4, var15);
         manager.addAxiom(ontology, var17);
         OWLNamedIndividual var18;
         OWLClassAssertionAxiom var19;
         if (end == null) {
            var18 = factory.getOWLNamedIndividual(":EU" + id(), pm);
            var19 = factory.getOWLClassAssertionAxiom(var5, var18);
            manager.addAxiom(ontology, var19);
         } else {
            var18 = factory.getOWLNamedIndividual(":Ins" + id(), pm);
            var19 = factory.getOWLClassAssertionAxiom(var5, var18);
            manager.addAxiom(ontology, var19);
            OWLLiteral var11 = factory.getOWLLiteral(end, OWL2Datatype.XSD_DATE_TIME);
            OWLDataProperty var12 = factory.getOWLDataProperty(":inXSDDateTime", tpm);
            OWLDataPropertyAssertionAxiom var13 = factory.getOWLDataPropertyAssertionAxiom(var12, var18, var11);
            manager.addAxiom(ontology, var13);
         }

         OWLObjectProperty var20 = factory.getOWLObjectProperty(":hasEnd", tpm);
         OWLObjectPropertyAssertionAxiom var21 = factory.getOWLObjectPropertyAssertionAxiom(var20, var4, var18);
         manager.addAxiom(ontology, var21);
         if (qual != null && interv != null) {
            OWLObjectProperty var22 = factory.getOWLObjectProperty(":" + qual, tpm);
            OWLObjectPropertyAssertionAxiom var14 = factory.getOWLObjectPropertyAssertionAxiom(var22, var4, interv);
            manager.addAxiom(ontology, var14);
         }
      }

      return var4;
   }

   public static boolean isTemporalFunctionalObjectProperty(OWLObjectProperty var0) {
      return ontology.containsAxiom(ConstraintFactory.getTemporalFunctionalObjectPropertyRule(var0));
   }

   public static boolean isTemporalInverseFunctionalObjectProperty(OWLObjectProperty var0) {
      return ontology.containsAxiom(ConstraintFactory.getTemporalInverseFunctionalObjectPropertyRule(var0));
   }

   public static boolean isTemporalTransitiveObjectProperty(OWLObjectProperty var0) {
      return ontology.containsAxiom(ConstraintFactory.getTemporalTransitiveObjectPropertyRule(var0));
   }

   private static void applyConstraintsOnObjectProperty(OWLObjectProperty property) {
      Set set;
      SWRLRule rule;
      if (!ontology.getFunctionalObjectPropertyAxioms(property).isEmpty()) {
         set = ontology.getFunctionalObjectPropertyAxioms(property);
         manager.removeAxioms(ontology, set);
         rule = ConstraintFactory.getTemporalFunctionalObjectPropertyRule(property);
         manager.addAxiom(ontology, rule);
      }
      if (!ontology.getInverseFunctionalObjectPropertyAxioms(property).isEmpty()) {
         set = ontology.getInverseFunctionalObjectPropertyAxioms(property);
         manager.removeAxioms(ontology, set);
         rule = ConstraintFactory.getTemporalInverseFunctionalObjectPropertyRule(property);
         manager.addAxiom(ontology, rule);
      }
      if (!ontology.getTransitiveObjectPropertyAxioms(property).isEmpty()) {
         set = ontology.getTransitiveObjectPropertyAxioms(property);
         manager.removeAxioms(ontology, set);
         rule = ConstraintFactory.getTemporalTransitiveObjectPropertyRule(property);
         manager.addAxiom(ontology, rule);
      }

      set = getClassConstraintsOnObjectProperty(property);

      for (Object o : set) {
         ConstraintStructure constraint = (ConstraintStructure) o;
         if (constraint.getConstraintAs() == ConstraintStructure.ConstraintAs.EQUIV) {
            OWLEquivalentClassesAxiom ax = factory.getOWLEquivalentClassesAxiom(constraint.getDomain(), constraint.getConstraint());
            manager.removeAxiom(ontology, ax);
            manager.addAxioms(ontology, ConstraintFactory.getTemporalEquivalentClassConstraint(constraint));
         } else if (constraint.getConstraintAs() == ConstraintStructure.ConstraintAs.SUPER) {
            OWLSubClassOfAxiom ax = factory.getOWLSubClassOfAxiom(constraint.getDomain(), constraint.getConstraint());
            manager.removeAxiom(ontology, ax);
            manager.addAxioms(ontology, ConstraintFactory.getTemporalSuperClassConstraint(constraint));
         }
      }

   }

   private static void applyConstraintsOnDataProperty(OWLDataProperty var0) {
      Set var1;
      if (!ontology.getFunctionalDataPropertyAxioms(var0).isEmpty()) {
         var1 = ontology.getFunctionalDataPropertyAxioms(var0);
         manager.removeAxioms(ontology, var1);
         SWRLRule var2 = ConstraintFactory.getTemporalFunctionalDataPropertyRule(var0);
         manager.addAxiom(ontology, var2);
      }

      var1 = getClassConstraintsOnDataProperty(var0);

      for (Object o : var1) {
         ConstraintStructure var3 = (ConstraintStructure) o;
         if (var3.getConstraintAs() == ConstraintStructure.ConstraintAs.SUPER) {
            OWLSubClassOfAxiom var4 = factory.getOWLSubClassOfAxiom(var3.getDomain(), var3.getConstraint());
            manager.removeAxiom(ontology, var4);
            manager.addAxioms(ontology, ConstraintFactory.getTemporalSuperClassConstraint(var3));
         } else if (var3.getConstraintAs() == ConstraintStructure.ConstraintAs.EQUIV) {
            OWLEquivalentClassesAxiom var6 = factory.getOWLEquivalentClassesAxiom(var3.getDomain(), var3.getConstraint());
            manager.removeAxiom(ontology, var6);
            manager.addAxioms(ontology, ConstraintFactory.getTemporalEquivalentClassConstraint(var3));
         }
      }

   }

   public static Set getClassConstraintsOnObjectProperty(OWLObjectProperty property) {
      Set<ConstraintStructure> constraintStructures = new HashSet<>();

      for (OWLClass owlClass : ontology.getClassesInSignature()) {
         OWLObjectAllValuesFrom var6;
         OWLObjectSomeValuesFrom var7;
         OWLObjectMaxCardinality var8;
         OWLObjectExactCardinality var9;
         OWLObjectMinCardinality var10;
         OWLObjectHasValue var11;

         for (OWLClass superClass : reasoner.getSuperClasses(owlClass, true).getFlattened()) {
            if (superClass.getClassExpressionType() == ClassExpressionType.OBJECT_ALL_VALUES_FROM) {
               var6 = (OWLObjectAllValuesFrom) superClass;
               if (var6.getProperty() == property) {
                  constraintStructures.add(new ObjectAllValuesFromStructure(owlClass, property, (OWLClassExpression) var6.getFiller(), superClass, ConstraintStructure.ConstraintAs.SUPER));
               }
            } else if (superClass.getClassExpressionType() == ClassExpressionType.OBJECT_SOME_VALUES_FROM) {
               var7 = (OWLObjectSomeValuesFrom) superClass;
               if (var7.getProperty() == property) {
                  constraintStructures.add(new ObjectSomeValuesFromStructure(owlClass, property, (OWLClassExpression) var7.getFiller(), superClass, ConstraintStructure.ConstraintAs.SUPER));
               }
            } else if (superClass.getClassExpressionType() == ClassExpressionType.OBJECT_MAX_CARDINALITY) {
               var8 = (OWLObjectMaxCardinality) superClass;
               if (var8.getProperty() == property) {
                  constraintStructures.add(new ObjectMaxCardinalityStructure(owlClass, property, var8.getCardinality(), (OWLClassExpression) var8.getFiller(), superClass, ConstraintStructure.ConstraintAs.SUPER));
               }
            } else if (superClass.getClassExpressionType() == ClassExpressionType.OBJECT_EXACT_CARDINALITY) {
               var9 = (OWLObjectExactCardinality) superClass;
               if (var9.getProperty() == property) {
                  constraintStructures.add(new ObjectExactCardinalityStructure(owlClass, property, var9.getCardinality(), (OWLClassExpression) var9.getFiller(), superClass, ConstraintStructure.ConstraintAs.SUPER));
               }
            } else if (superClass.getClassExpressionType() == ClassExpressionType.OBJECT_MIN_CARDINALITY) {
               var10 = (OWLObjectMinCardinality) superClass;
               if (var10.getProperty() == property) {
                  constraintStructures.add(new ObjectMinCardinalityStructure(owlClass, property, var10.getCardinality(), (OWLClassExpression) var10.getFiller(), superClass, ConstraintStructure.ConstraintAs.SUPER));
               }
            } else if (superClass.getClassExpressionType() == ClassExpressionType.OBJECT_HAS_VALUE) {
               var11 = (OWLObjectHasValue) superClass;
               if (var11.getProperty() == property) {
                  constraintStructures.add(new ObjectHasValueStructure(owlClass, property, ((OWLIndividual) var11.getValue()).asOWLNamedIndividual(), superClass, ConstraintStructure.ConstraintAs.SUPER));
               }
            }
         }


         for (OWLClass eqClass : reasoner.getEquivalentClasses(owlClass)) {
            if (eqClass.getClassExpressionType() == ClassExpressionType.OBJECT_ALL_VALUES_FROM) {
               var6 = (OWLObjectAllValuesFrom) eqClass;
               if (var6.getProperty() == property) {
                  constraintStructures.add(new ObjectAllValuesFromStructure(owlClass, property, (OWLClassExpression) var6.getFiller(), eqClass, ConstraintStructure.ConstraintAs.EQUIV));
               }
            } else if (eqClass.getClassExpressionType() == ClassExpressionType.OBJECT_SOME_VALUES_FROM) {
               var7 = (OWLObjectSomeValuesFrom) eqClass;
               if (var7.getProperty() == property) {
                  constraintStructures.add(new ObjectSomeValuesFromStructure(owlClass, property, (OWLClassExpression) var7.getFiller(), eqClass, ConstraintStructure.ConstraintAs.EQUIV));
               }
            } else if (eqClass.getClassExpressionType() == ClassExpressionType.OBJECT_MAX_CARDINALITY) {
               var8 = (OWLObjectMaxCardinality) eqClass;
               if (var8.getProperty() == property) {
                  constraintStructures.add(new ObjectMaxCardinalityStructure(owlClass, property, var8.getCardinality(), (OWLClassExpression) var8.getFiller(), eqClass, ConstraintStructure.ConstraintAs.EQUIV));
               }
            } else if (eqClass.getClassExpressionType() == ClassExpressionType.OBJECT_EXACT_CARDINALITY) {
               var9 = (OWLObjectExactCardinality) eqClass;
               if (var9.getProperty() == property) {
                  constraintStructures.add(new ObjectExactCardinalityStructure(owlClass, property, var9.getCardinality(), (OWLClassExpression) var9.getFiller(), eqClass, ConstraintStructure.ConstraintAs.EQUIV));
               }
            } else if (eqClass.getClassExpressionType() == ClassExpressionType.OBJECT_MIN_CARDINALITY) {
               var10 = (OWLObjectMinCardinality) eqClass;
               if (var10.getProperty() == property) {
                  constraintStructures.add(new ObjectMinCardinalityStructure(owlClass, property, var10.getCardinality(), (OWLClassExpression) var10.getFiller(), eqClass, ConstraintStructure.ConstraintAs.EQUIV));
               }
            } else if (eqClass.getClassExpressionType() == ClassExpressionType.OBJECT_HAS_VALUE) {
               var11 = (OWLObjectHasValue) eqClass;
               if (var11.getProperty() == property) {
                  constraintStructures.add(new ObjectHasValueStructure(owlClass, property, ((OWLIndividual) var11.getValue()).asOWLNamedIndividual(), eqClass, ConstraintStructure.ConstraintAs.EQUIV));
               }
            }
         }
      }

      return constraintStructures;
   }

   public static Set getClassConstraintsOnDataProperty(OWLDataProperty var0) {
      HashSet var1 = new HashSet();
      Iterator var2 = ontology.getClassesInSignature().iterator();

      while(var2.hasNext()) {
         OWLClass var3 = (OWLClass)var2.next();
         Iterator var4 = reasoner.getSuperClasses(var3, true).iterator();

         OWLClassExpression var5;
         OWLDataAllValuesFrom var6;
         OWLDataSomeValuesFrom var7;
         OWLDataMaxCardinality var8;
         OWLDataExactCardinality var9;
         OWLDataMinCardinality var10;
         OWLDataHasValue var11;
         while(var4.hasNext()) {
            var5 = (OWLClassExpression)var4.next();
            if (var5.getClassExpressionType() == ClassExpressionType.DATA_ALL_VALUES_FROM) {
               var6 = (OWLDataAllValuesFrom)var5;
               if (var6.getProperty() == var0) {
                  var1.add(new DataAllValuesFromStructure(var3, var0, ((OWLDataRange)var6.getFiller()).asOWLDatatype(), var5, ConstraintStructure.ConstraintAs.SUPER));
               }
            } else if (var5.getClassExpressionType() == ClassExpressionType.DATA_SOME_VALUES_FROM) {
               var7 = (OWLDataSomeValuesFrom)var5;
               if (var7.getProperty() == var0) {
                  var1.add(new DataSomeValuesFromStructure(var3, var0, ((OWLDataRange)var7.getFiller()).asOWLDatatype(), var5, ConstraintStructure.ConstraintAs.SUPER));
               }
            } else if (var5.getClassExpressionType() == ClassExpressionType.DATA_MAX_CARDINALITY) {
               var8 = (OWLDataMaxCardinality)var5;
               if (var8.getProperty() == var0) {
                  var1.add(new DataMaxCardinalityStructure(var3, var0, var8.getCardinality(), ((OWLDataRange)var8.getFiller()).asOWLDatatype(), var5, ConstraintStructure.ConstraintAs.SUPER));
               }
            } else if (var5.getClassExpressionType() == ClassExpressionType.DATA_EXACT_CARDINALITY) {
               var9 = (OWLDataExactCardinality)var5;
               if (var9.getProperty() == var0) {
                  var1.add(new DataExactCardinalityStructure(var3, var0, var9.getCardinality(), ((OWLDataRange)var9.getFiller()).asOWLDatatype(), var5, ConstraintStructure.ConstraintAs.SUPER));
               }
            } else if (var5.getClassExpressionType() == ClassExpressionType.DATA_MIN_CARDINALITY) {
               var10 = (OWLDataMinCardinality)var5;
               if (var10.getProperty() == var0) {
                  var1.add(new DataMinCardinalityStructure(var3, var0, var10.getCardinality(), ((OWLDataRange)var10.getFiller()).asOWLDatatype(), var5, ConstraintStructure.ConstraintAs.SUPER));
               }
            } else if (var5.getClassExpressionType() == ClassExpressionType.DATA_HAS_VALUE) {
               var11 = (OWLDataHasValue)var5;
               if (var11.getProperty() == var0) {
                  var1.add(new DataHasValueStructure(var3, var0, ((OWLLiteral)var11.getValue()).getLiteral(), var5, ConstraintStructure.ConstraintAs.SUPER));
               }
            }
         }

         var4 = reasoner.getEquivalentClasses(var3).iterator();

         while(var4.hasNext()) {
            var5 = (OWLClassExpression)var4.next();
            if (var5.getClassExpressionType() == ClassExpressionType.DATA_ALL_VALUES_FROM) {
               var6 = (OWLDataAllValuesFrom)var5;
               if (var6.getProperty() == var0) {
                  var1.add(new DataAllValuesFromStructure(var3, var0, ((OWLDataRange)var6.getFiller()).asOWLDatatype(), var5, ConstraintStructure.ConstraintAs.EQUIV));
               }
            } else if (var5.getClassExpressionType() == ClassExpressionType.DATA_SOME_VALUES_FROM) {
               var7 = (OWLDataSomeValuesFrom)var5;
               if (var7.getProperty() == var0) {
                  var1.add(new DataSomeValuesFromStructure(var3, var0, ((OWLDataRange)var7.getFiller()).asOWLDatatype(), var5, ConstraintStructure.ConstraintAs.EQUIV));
               }
            } else if (var5.getClassExpressionType() == ClassExpressionType.DATA_MAX_CARDINALITY) {
               var8 = (OWLDataMaxCardinality)var5;
               if (var8.getProperty() == var0) {
                  var1.add(new DataMaxCardinalityStructure(var3, var0, var8.getCardinality(), ((OWLDataRange)var8.getFiller()).asOWLDatatype(), var5, ConstraintStructure.ConstraintAs.EQUIV));
               }
            } else if (var5.getClassExpressionType() == ClassExpressionType.DATA_EXACT_CARDINALITY) {
               var9 = (OWLDataExactCardinality)var5;
               if (var9.getProperty() == var0) {
                  var1.add(new DataExactCardinalityStructure(var3, var0, var9.getCardinality(), ((OWLDataRange)var9.getFiller()).asOWLDatatype(), var5, ConstraintStructure.ConstraintAs.EQUIV));
               }
            } else if (var5.getClassExpressionType() == ClassExpressionType.DATA_MIN_CARDINALITY) {
               var10 = (OWLDataMinCardinality)var5;
               if (var10.getProperty() == var0) {
                  var1.add(new DataMinCardinalityStructure(var3, var0, var10.getCardinality(), ((OWLDataRange)var10.getFiller()).asOWLDatatype(), var5, ConstraintStructure.ConstraintAs.EQUIV));
               }
            } else if (var5.getClassExpressionType() == ClassExpressionType.DATA_HAS_VALUE) {
               var11 = (OWLDataHasValue)var5;
               if (var11.getProperty() == var0) {
                  var1.add(new DataHasValueStructure(var3, var0, ((OWLLiteral)var11.getValue()).getLiteral(), var5, ConstraintStructure.ConstraintAs.EQUIV));
               }
            }
         }
      }

      return var1;
   }

   public static Set getRelatedProperties(OWLClass var0) {
      HashSet var1 = new HashSet();
      Set var2 = (Set) reasoner.getInstances(var0, true);
      HashSet var3 = new HashSet();
      Iterator var4 = reasoner.getSubObjectProperties(dataPropertyOP, true).iterator();

      while(var4.hasNext()) {
         OWLObjectPropertyExpression var5 = (OWLObjectPropertyExpression)var4.next();
         var3.add(var5.asOWLObjectProperty());
      }

      var4 = var2.iterator();

      while(var4.hasNext()) {
         OWLIndividual var8 = (OWLIndividual)var4.next();
         for (OWLObjectProperty p : ontology.getObjectPropertiesInSignature()) {
            Iterator var6 = reasoner.getObjectPropertyValues((OWLNamedIndividual) var8, p).iterator();

            while (var6.hasNext()) {
               OWLObjectPropertyExpression var7 = (OWLObjectPropertyExpression) var6.next();
               if (!var7.isAnonymous() && !isTemporalProperty(var7.asOWLObjectProperty()) && !var3.contains(var7)) {
                  var1.add(var7.asOWLObjectProperty());
               }
            }
         }

         for (OWLDataProperty p : ontology.getDataPropertiesInSignature()) {
            Iterator var6 = reasoner.getDataPropertyValues((OWLNamedIndividual) var8, p).iterator();

            while (var6.hasNext()) {
               OWLDataPropertyExpression var13 = (OWLDataPropertyExpression) var6.next();
               if (!var13.isAnonymous() && !isTemporalProperty(var13.asOWLDataProperty())) {
                  var1.add(var13.asOWLDataProperty());
               }
            }
         }
      }

      var4 = ontology.getReferencingAxioms(var0).iterator();

      while(var4.hasNext()) {
         OWLAxiom var9 = (OWLAxiom)var4.next();
         if (var9.isOfType(new AxiomType[]{AxiomType.OBJECT_PROPERTY_DOMAIN})) {
            OWLObjectPropertyDomainAxiom var11 = (OWLObjectPropertyDomainAxiom)var9;
            if (!((OWLObjectPropertyExpression)var11.getProperty()).isAnonymous() && !isTemporalProperty(((OWLObjectPropertyExpression)var11.getProperty()).asOWLObjectProperty()) && !var3.contains(((OWLObjectPropertyExpression)var11.getProperty()).asOWLObjectProperty())) {
               var1.add(((OWLObjectPropertyExpression)var11.getProperty()).asOWLObjectProperty());
            }
         } else if (var9.isOfType(new AxiomType[]{AxiomType.DATA_PROPERTY_DOMAIN})) {
            OWLDataPropertyDomainAxiom var12 = (OWLDataPropertyDomainAxiom)var9;
            if (!((OWLDataPropertyExpression)var12.getProperty()).isAnonymous() && !isTemporalProperty(((OWLDataPropertyExpression)var12.getProperty()).asOWLDataProperty())) {
               var1.add(((OWLDataPropertyExpression)var12.getProperty()).asOWLDataProperty());
            }
         }
      }

      var4 = reasoner.getEquivalentClasses(var0).iterator();

      while(true) {
         OWLClassExpression var10;
         OWLDataProperty var14;
         OWLObjectProperty var15;
         while(var4.hasNext()) {
            var10 = (OWLClassExpression)var4.next();
            if (var10.isAnonymous() && !var10.getObjectPropertiesInSignature().isEmpty()) {
               var15 = (OWLObjectProperty)var10.getObjectPropertiesInSignature().iterator().next();
               if (!isTemporalProperty(var15)) {
                  var1.add(var15);
               }
            } else if (var10.isAnonymous() && !var10.getDataPropertiesInSignature().isEmpty()) {
               var14 = (OWLDataProperty)var10.getDataPropertiesInSignature().iterator().next();
               if (!isTemporalProperty(var14)) {
                  var1.add(var14);
               }
            }
         }

         var4 = reasoner.getSuperClasses(var0, true).iterator();

         while(true) {
            while(var4.hasNext()) {
               var10 = (OWLClassExpression)var4.next();
               if (var10.isAnonymous() && !var10.getObjectPropertiesInSignature().isEmpty()) {
                  var15 = (OWLObjectProperty)var10.getObjectPropertiesInSignature().iterator().next();
                  if (!isTemporalProperty(var15)) {
                     var1.add(var15);
                  }
               } else if (var10.isAnonymous() && !var10.getDataPropertiesInSignature().isEmpty()) {
                  var14 = (OWLDataProperty)var10.getDataPropertiesInSignature().iterator().next();
                  if (!isTemporalProperty(var14)) {
                     var1.add(var14);
                  }
               }
            }

            return var1;
         }
      }
   }

   public static void convertClassToTemporal(OWLClass var0, OWLNamedIndividual var1) {
      Set var2 = getRelatedProperties(var0);
      Iterator var3 = var2.iterator();

      while(var3.hasNext()) {
         OWLEntity var4 = (OWLEntity)var3.next();
         if (var4.isOWLObjectProperty()) {
            convertObjectPropertyToTemporal(var4.asOWLObjectProperty(), var1);
         } else {
            convertDataPropertyToTemporal(var4.asOWLDataProperty(), var1);
         }
      }

   }

   public static boolean isTemporalClass(OWLClass var0) {
      Set var1 = getRelatedProperties(var0);
      return var1.isEmpty();
   }

   public static long id() {
      long var0 = System.currentTimeMillis();
      if (lastId >= var0) {
         var0 = lastId + 1L;
      }

      lastId = var0;
      return var0;
   }
}
