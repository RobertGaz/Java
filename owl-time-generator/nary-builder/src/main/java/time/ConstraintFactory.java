package time;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.protege.editor.owl.model.OWLModelManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataAllValuesFrom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataRange;
import org.semanticweb.owlapi.model.OWLDataSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLObjectAllValuesFrom;
import org.semanticweb.owlapi.model.OWLObjectIntersectionOf;
import org.semanticweb.owlapi.model.OWLObjectMinCardinality;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.model.PrefixManager;
import org.semanticweb.owlapi.model.SWRLIArgument;
import org.semanticweb.owlapi.model.SWRLIndividualArgument;
import org.semanticweb.owlapi.model.SWRLLiteralArgument;
import org.semanticweb.owlapi.model.SWRLRule;
import org.semanticweb.owlapi.model.SWRLVariable;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.util.DefaultPrefixManager;
import org.semanticweb.owlapi.vocab.SWRLBuiltInsVocabulary;
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
import structures.TemporalNegativeDataPropertyTriple;
import structures.TemporalNegativeObjectPropertyTriple;
import ui.Renderer;

public class ConstraintFactory {
   private static OWLReasoner reasoner;
   private static OWLOntologyManager manager;
   private static OWLDataFactory factory;
   private static OWLOntology ontology;
   private static PrefixManager pm;
   private static PrefixManager tpm;
   private static PrefixManager intellipm;
   private static OWLClass event;
   private static OWLObjectProperty during;
   private static OWLObjectProperty overlaps;
   private static OWLObjectProperty participatesIn;
   private static OWLAnnotationProperty superClassConstraint;
   private static OWLAnnotationProperty equivClassConstraint;
   private static String baseIRI;

   public static void initialize(OWLOntologyManager owlOntologyManager, OWLOntology owlOntology, OWLReasoner owlReasoner) {
      reasoner = owlReasoner;
      ontology = owlOntology;
      manager = owlOntologyManager;
      factory = manager.getOWLDataFactory();
      pm = new DefaultPrefixManager(ontology.getOntologyID().getOntologyIRI().toString() + "#");
      tpm = new DefaultPrefixManager("http://www.w3.org/2006/time#");
      baseIRI = pm.getDefaultPrefix();
      intellipm = new DefaultPrefixManager("http://www.intelligence.tuc.gr/2011/timeEvents#");
      event = factory.getOWLClass(":Event", intellipm);
      during = factory.getOWLObjectProperty(":during", intellipm);
      overlaps = factory.getOWLObjectProperty(":overlaps", intellipm);
      participatesIn = factory.getOWLObjectProperty(":participatesIn", intellipm);
      superClassConstraint = factory.getOWLAnnotationProperty(":superClassConstraint", pm);
      equivClassConstraint = factory.getOWLAnnotationProperty(":equivClassConstraint", pm);
   }

   public static SWRLRule getTemporalFunctionalObjectPropertyRule(OWLObjectProperty var0) {
      SWRLVariable var1 = factory.getSWRLVariable(IRI.create(baseIRI + "x"));
      SWRLVariable var2 = factory.getSWRLVariable(IRI.create(baseIRI + "y1"));
      SWRLVariable var3 = factory.getSWRLVariable(IRI.create(baseIRI + "y2"));
      SWRLVariable var4 = factory.getSWRLVariable(IRI.create(baseIRI + "e1"));
      SWRLVariable var5 = factory.getSWRLVariable(IRI.create(baseIRI + "e2"));
      SWRLVariable var6 = factory.getSWRLVariable(IRI.create(baseIRI + "i1"));
      SWRLVariable var7 = factory.getSWRLVariable(IRI.create(baseIRI + "i2"));
      HashSet var8 = new HashSet();
      var8.add(factory.getSWRLObjectPropertyAtom(var0, var1, var4));
      var8.add(factory.getSWRLClassAtom(event, var4));
      var8.add(factory.getSWRLObjectPropertyAtom(var0, var4, var2));
      var8.add(factory.getSWRLObjectPropertyAtom(during, var4, var6));
      var8.add(factory.getSWRLObjectPropertyAtom(var0, var1, var5));
      var8.add(factory.getSWRLClassAtom(event, var5));
      var8.add(factory.getSWRLObjectPropertyAtom(var0, var5, var3));
      var8.add(factory.getSWRLObjectPropertyAtom(during, var5, var7));
      var8.add(factory.getSWRLObjectPropertyAtom(overlaps, var6, var7));
      SWRLRule var9 = factory.getSWRLRule(var8, Collections.singleton(factory.getSWRLSameIndividualAtom(var2, var3)));
      return var9;
   }

   public static SWRLRule getTemporalInverseFunctionalObjectPropertyRule(OWLObjectProperty var0) {
      SWRLVariable var1 = factory.getSWRLVariable(IRI.create(baseIRI + "x1"));
      SWRLVariable var2 = factory.getSWRLVariable(IRI.create(baseIRI + "x2"));
      SWRLVariable var3 = factory.getSWRLVariable(IRI.create(baseIRI + "y"));
      SWRLVariable var4 = factory.getSWRLVariable(IRI.create(baseIRI + "e1"));
      SWRLVariable var5 = factory.getSWRLVariable(IRI.create(baseIRI + "e2"));
      SWRLVariable var6 = factory.getSWRLVariable(IRI.create(baseIRI + "i1"));
      SWRLVariable var7 = factory.getSWRLVariable(IRI.create(baseIRI + "i2"));
      HashSet var8 = new HashSet();
      var8.add(factory.getSWRLObjectPropertyAtom(var0, var1, var4));
      var8.add(factory.getSWRLClassAtom(event, var4));
      var8.add(factory.getSWRLObjectPropertyAtom(var0, var4, var3));
      var8.add(factory.getSWRLObjectPropertyAtom(during, var4, var6));
      var8.add(factory.getSWRLObjectPropertyAtom(var0, var2, var5));
      var8.add(factory.getSWRLClassAtom(event, var5));
      var8.add(factory.getSWRLObjectPropertyAtom(var0, var5, var3));
      var8.add(factory.getSWRLObjectPropertyAtom(during, var5, var7));
      var8.add(factory.getSWRLObjectPropertyAtom(overlaps, var6, var7));
      SWRLRule var9 = factory.getSWRLRule(var8, Collections.singleton(factory.getSWRLSameIndividualAtom(var1, var2)));
      return var9;
   }

   public static SWRLRule getTemporalTransitiveObjectPropertyRule(OWLObjectProperty var0) {
      SWRLVariable var1 = factory.getSWRLVariable(IRI.create(baseIRI + "x"));
      SWRLVariable var2 = factory.getSWRLVariable(IRI.create(baseIRI + "y"));
      SWRLVariable var3 = factory.getSWRLVariable(IRI.create(baseIRI + "z"));
      SWRLVariable var4 = factory.getSWRLVariable(IRI.create(baseIRI + "e1"));
      SWRLVariable var5 = factory.getSWRLVariable(IRI.create(baseIRI + "e2"));
      SWRLVariable var6 = factory.getSWRLVariable(IRI.create(baseIRI + "i1"));
      SWRLVariable var7 = factory.getSWRLVariable(IRI.create(baseIRI + "i2"));
      HashSet var8 = new HashSet();
      var8.add(factory.getSWRLObjectPropertyAtom(var0, var1, var4));
      var8.add(factory.getSWRLClassAtom(event, var4));
      var8.add(factory.getSWRLObjectPropertyAtom(var0, var4, var2));
      var8.add(factory.getSWRLObjectPropertyAtom(during, var4, var6));
      var8.add(factory.getSWRLObjectPropertyAtom(var0, var2, var5));
      var8.add(factory.getSWRLClassAtom(event, var5));
      var8.add(factory.getSWRLObjectPropertyAtom(var0, var5, var3));
      var8.add(factory.getSWRLObjectPropertyAtom(during, var5, var7));
      var8.add(factory.getSWRLObjectPropertyAtom(factory.getOWLObjectProperty(":intervalEquals", tpm), var6, var7));
      var8.add(factory.getSWRLDifferentIndividualsAtom(var2, var3));
      SWRLRule var9 = factory.getSWRLRule(var8, Collections.singleton(factory.getSWRLObjectPropertyAtom(var0, var1, var5)));
      return var9;
   }

   public static SWRLRule getTemporalFunctionalDataPropertyRule(OWLDataProperty var0) {
      SWRLVariable var1 = factory.getSWRLVariable(IRI.create(baseIRI + "x"));
      SWRLVariable var2 = factory.getSWRLVariable(IRI.create(baseIRI + "y1"));
      SWRLVariable var3 = factory.getSWRLVariable(IRI.create(baseIRI + "y2"));
      SWRLVariable var4 = factory.getSWRLVariable(IRI.create(baseIRI + "e1"));
      SWRLVariable var5 = factory.getSWRLVariable(IRI.create(baseIRI + "e2"));
      SWRLVariable var6 = factory.getSWRLVariable(IRI.create(baseIRI + "i1"));
      SWRLVariable var7 = factory.getSWRLVariable(IRI.create(baseIRI + "i2"));
      OWLObjectProperty var8 = factory.getOWLObjectProperty(":" + var0.getIRI().getFragment() + "OP", pm);
      HashSet var9 = new HashSet();
      var9.add(factory.getSWRLObjectPropertyAtom(var8, var1, var4));
      var9.add(factory.getSWRLClassAtom(event, var4));
      var9.add(factory.getSWRLDataPropertyAtom(var0, var4, var2));
      var9.add(factory.getSWRLObjectPropertyAtom(during, var4, var6));
      var9.add(factory.getSWRLObjectPropertyAtom(var8, var1, var5));
      var9.add(factory.getSWRLClassAtom(event, var5));
      var9.add(factory.getSWRLDataPropertyAtom(var0, var5, var3));
      var9.add(factory.getSWRLObjectPropertyAtom(during, var5, var7));
      var9.add(factory.getSWRLObjectPropertyAtom(overlaps, var6, var7));
      ArrayList var10 = new ArrayList();
      var10.add(var2);
      var10.add(var3);
      var9.add(factory.getSWRLBuiltInAtom(SWRLBuiltInsVocabulary.NOT_EQUAL.getIRI(), var10));
      SWRLRule var11 = factory.getSWRLRule(var9, Collections.singleton(factory.getSWRLClassAtom(factory.getOWLNothing(), var1)));
      return var11;
   }

   public static SWRLRule getTemporalObjectMaxCardinalityRule(ConstraintStructure var0) {
      ObjectMaxCardinalityStructure var1 = (ObjectMaxCardinalityStructure)var0;
      OWLObjectProperty var2 = var1.getProperty();
      int var3 = var1.getCardinality();
      HashSet var4 = new HashSet();
      ArrayList var5 = new ArrayList();
      ArrayList var6 = new ArrayList();
      SWRLVariable var7 = factory.getSWRLVariable(IRI.create(baseIRI + "x"));
      var4.add(factory.getSWRLClassAtom(var0.getDomain(), var7));

      int var8;
      SWRLVariable var9;
      SWRLVariable var10;
      for(var8 = 0; var8 <= var3; ++var8) {
         var9 = factory.getSWRLVariable(IRI.create(baseIRI + "y" + var8));
         var10 = factory.getSWRLVariable(IRI.create(baseIRI + "e" + var8));
         SWRLVariable var11 = factory.getSWRLVariable(IRI.create(baseIRI + "i" + var8));
         var6.add(var9);
         var5.add(var11);
         var4.add(factory.getSWRLObjectPropertyAtom(var2, var7, var10));
         var4.add(factory.getSWRLClassAtom(event, var10));
         var4.add(factory.getSWRLObjectPropertyAtom(var2, var10, var9));
         var4.add(factory.getSWRLClassAtom(var1.getRange(), var9));
         var4.add(factory.getSWRLObjectPropertyAtom(during, var10, var11));
      }

      if (var5.size() > 1) {
         for(var8 = 0; var8 < var5.size(); ++var8) {
            var9 = (SWRLVariable)var5.get(var8);
            var10 = (SWRLVariable)var6.get(var8);

            for(int var12 = var8 + 1; var12 < var5.size(); ++var12) {
               var4.add(factory.getSWRLObjectPropertyAtom(overlaps, var9, (SWRLIArgument)var5.get(var12)));
               var4.add(factory.getSWRLDifferentIndividualsAtom(var10, (SWRLIArgument)var6.get(var12)));
            }
         }
      }

      SWRLRule var13 = factory.getSWRLRule(var4, Collections.singleton(factory.getSWRLClassAtom(factory.getOWLNothing(), var7)));
      return var13;
   }

   public static SWRLRule getTemporalObjectExactCardinalityRule(ConstraintStructure var0) {
      ObjectExactCardinalityStructure var1 = (ObjectExactCardinalityStructure)var0;
      OWLObjectProperty var2 = var1.getProperty();
      int var3 = var1.getCardinality();
      HashSet var4 = new HashSet();
      ArrayList var5 = new ArrayList();
      ArrayList var6 = new ArrayList();
      SWRLVariable var7 = factory.getSWRLVariable(IRI.create(baseIRI + "x"));
      var4.add(factory.getSWRLClassAtom(var0.getDomain(), var7));

      int var8;
      SWRLVariable var9;
      SWRLVariable var10;
      for(var8 = 0; var8 <= var3; ++var8) {
         var9 = factory.getSWRLVariable(IRI.create(baseIRI + "y" + var8));
         var10 = factory.getSWRLVariable(IRI.create(baseIRI + "e" + var8));
         SWRLVariable var11 = factory.getSWRLVariable(IRI.create(baseIRI + "i" + var8));
         var6.add(var9);
         var5.add(var11);
         var4.add(factory.getSWRLObjectPropertyAtom(var2, var7, var10));
         var4.add(factory.getSWRLClassAtom(event, var10));
         var4.add(factory.getSWRLObjectPropertyAtom(var2, var10, var9));
         var4.add(factory.getSWRLClassAtom(var1.getRange(), var9));
         var4.add(factory.getSWRLObjectPropertyAtom(during, var10, var11));
      }

      if (var5.size() > 1) {
         for(var8 = 0; var8 < var5.size(); ++var8) {
            var9 = (SWRLVariable)var5.get(var8);
            var10 = (SWRLVariable)var6.get(var8);

            for(int var12 = var8 + 1; var12 < var5.size(); ++var12) {
               var4.add(factory.getSWRLObjectPropertyAtom(overlaps, var9, (SWRLIArgument)var5.get(var12)));
               var4.add(factory.getSWRLDifferentIndividualsAtom(var10, (SWRLIArgument)var6.get(var12)));
            }
         }
      }

      SWRLRule var13 = factory.getSWRLRule(var4, Collections.singleton(factory.getSWRLClassAtom(factory.getOWLNothing(), var7)));
      return var13;
   }

   public static SWRLRule getTemporalDataMaxCardinalityRule(ConstraintStructure var0) {
      DataMaxCardinalityStructure var1 = (DataMaxCardinalityStructure)var0;
      OWLDataProperty var2 = var1.getProperty();
      OWLObjectProperty var3 = factory.getOWLObjectProperty(":" + Renderer.getName(var2) + "OP", pm);
      int var4 = var1.getCardinality();
      HashSet var5 = new HashSet();
      ArrayList var6 = new ArrayList();
      ArrayList var7 = new ArrayList();
      SWRLVariable var8 = factory.getSWRLVariable(IRI.create(baseIRI + "x"));
      var5.add(factory.getSWRLClassAtom(var0.getDomain(), var8));

      int var9;
      SWRLVariable var10;
      for(var9 = 0; var9 <= var4; ++var9) {
         var10 = factory.getSWRLVariable(IRI.create(baseIRI + "y" + var9));
         SWRLVariable var11 = factory.getSWRLVariable(IRI.create(baseIRI + "e" + var9));
         SWRLVariable var12 = factory.getSWRLVariable(IRI.create(baseIRI + "i" + var9));
         var7.add(var10);
         var6.add(var12);
         var5.add(factory.getSWRLObjectPropertyAtom(var3, var8, var11));
         var5.add(factory.getSWRLClassAtom(event, var11));
         var5.add(factory.getSWRLDataPropertyAtom(var2, var11, var10));
         var5.add(factory.getSWRLObjectPropertyAtom(during, var11, var12));
      }

      if (var6.size() > 1) {
         for(var9 = 0; var9 < var6.size(); ++var9) {
            var10 = (SWRLVariable)var6.get(var9);

            for(int var14 = var9 + 1; var14 < var6.size(); ++var14) {
               ArrayList var15 = new ArrayList();
               var15.add(var7.get(var9));
               var15.add(var7.get(var14));
               var5.add(factory.getSWRLObjectPropertyAtom(overlaps, var10, (SWRLIArgument)var6.get(var14)));
               var5.add(factory.getSWRLBuiltInAtom(SWRLBuiltInsVocabulary.NOT_EQUAL.getIRI(), var15));
            }
         }
      }

      SWRLRule var13 = factory.getSWRLRule(var5, Collections.singleton(factory.getSWRLClassAtom(factory.getOWLNothing(), var8)));
      return var13;
   }

   public static SWRLRule getTemporalDataExactCardinalityRule(ConstraintStructure var0) {
      DataExactCardinalityStructure var1 = (DataExactCardinalityStructure)var0;
      OWLDataProperty var2 = var1.getProperty();
      OWLObjectProperty var3 = factory.getOWLObjectProperty(":" + Renderer.getName(var2) + "OP", pm);
      int var4 = var1.getCardinality();
      HashSet var5 = new HashSet();
      ArrayList var6 = new ArrayList();
      ArrayList var7 = new ArrayList();
      SWRLVariable var8 = factory.getSWRLVariable(IRI.create(baseIRI + "x"));
      var5.add(factory.getSWRLClassAtom(var0.getDomain(), var8));

      int var9;
      SWRLVariable var10;
      for(var9 = 0; var9 <= var4; ++var9) {
         var10 = factory.getSWRLVariable(IRI.create(baseIRI + "y" + var9));
         SWRLVariable var11 = factory.getSWRLVariable(IRI.create(baseIRI + "e" + var9));
         SWRLVariable var12 = factory.getSWRLVariable(IRI.create(baseIRI + "i" + var9));
         var7.add(var10);
         var6.add(var12);
         var5.add(factory.getSWRLObjectPropertyAtom(var3, var8, var11));
         var5.add(factory.getSWRLClassAtom(event, var11));
         var5.add(factory.getSWRLDataPropertyAtom(var2, var11, var10));
         var5.add(factory.getSWRLObjectPropertyAtom(during, var11, var12));
      }

      if (var6.size() > 1) {
         for(var9 = 0; var9 < var6.size(); ++var9) {
            var10 = (SWRLVariable)var6.get(var9);

            for(int var14 = var9 + 1; var14 < var6.size(); ++var14) {
               ArrayList var15 = new ArrayList();
               var15.add(var7.get(var9));
               var15.add(var7.get(var14));
               var5.add(factory.getSWRLObjectPropertyAtom(overlaps, var10, (SWRLIArgument)var6.get(var14)));
               var5.add(factory.getSWRLBuiltInAtom(SWRLBuiltInsVocabulary.NOT_EQUAL.getIRI(), var15));
            }
         }
      }

      SWRLRule var13 = factory.getSWRLRule(var5, Collections.singleton(factory.getSWRLClassAtom(factory.getOWLNothing(), var8)));
      return var13;
   }

   public static SWRLRule getTemporalObjectHasValueRule(ConstraintStructure var0) {
      ObjectHasValueStructure var1 = (ObjectHasValueStructure)var0;
      SWRLVariable var2 = factory.getSWRLVariable(IRI.create(baseIRI + "x"));
      SWRLVariable var3 = factory.getSWRLVariable(IRI.create(baseIRI + "e"));
      SWRLIndividualArgument var4 = factory.getSWRLIndividualArgument(var1.getRange());
      HashSet var5 = new HashSet();
      var5.add(factory.getSWRLClassAtom(var1.getDomain(), var2));
      var5.add(factory.getSWRLObjectPropertyAtom(participatesIn, var2, var3));
      var5.add(factory.getSWRLClassAtom(event, var3));
      HashSet var6 = new HashSet();
      var6.add(factory.getSWRLObjectPropertyAtom(var1.getProperty(), var2, var3));
      var6.add(factory.getSWRLObjectPropertyAtom(var1.getProperty(), var3, var4));
      SWRLRule var7 = factory.getSWRLRule(var5, var6);
      return var7;
   }

   public static SWRLRule getTemporalDataHasValueRule(ConstraintStructure var0) {
      DataHasValueStructure var1 = (DataHasValueStructure)var0;
      OWLObjectProperty var2 = factory.getOWLObjectProperty(":" + Renderer.getName(var1.getProperty()) + "OP", pm);
      OWLDatatype var3 = null;
//      reasoner.get
//      if (var1.getProperty().getRanges(ontology).isEmpty()) {
//         var3 = factory.getTopDatatype();
//      } else {
//         var3 = ((OWLDataRange)var1.getProperty().getRanges(ontology).iterator().next()).asOWLDatatype();
//      }

      OWLLiteral var4 = factory.getOWLLiteral(var1.getRange(), var3);
      SWRLVariable var5 = factory.getSWRLVariable(IRI.create(baseIRI + "x"));
      SWRLVariable var6 = factory.getSWRLVariable(IRI.create(baseIRI + "e"));
      SWRLLiteralArgument var7 = factory.getSWRLLiteralArgument(var4);
      HashSet var8 = new HashSet();
      var8.add(factory.getSWRLClassAtom(var1.getDomain(), var5));
      var8.add(factory.getSWRLObjectPropertyAtom(participatesIn, var5, var6));
      var8.add(factory.getSWRLClassAtom(event, var6));
      HashSet var9 = new HashSet();
      var9.add(factory.getSWRLObjectPropertyAtom(var2, var5, var6));
      var9.add(factory.getSWRLDataPropertyAtom(var1.getProperty(), var6, var7));
      SWRLRule var10 = factory.getSWRLRule(var8, var9);
      return var10;
   }

   public static Set getTemporalSuperClassConstraint(ConstraintStructure var0) {
      HashSet var1 = new HashSet();
      OWLObjectIntersectionOf var4;
      OWLSubClassOfAxiom var6;
      if (var0.getConstraintType() == ConstraintStructure.ConstraintType.OBJECT_ALL_VALUES_FROM) {
         ObjectAllValuesFromStructure var2 = (ObjectAllValuesFromStructure)var0;
         OWLObjectAllValuesFrom var3 = factory.getOWLObjectAllValuesFrom(var2.getProperty(), var2.getRange());
         var4 = factory.getOWLObjectIntersectionOf(new OWLClassExpression[]{event, var3});
         OWLObjectAllValuesFrom var5 = factory.getOWLObjectAllValuesFrom(var2.getProperty(), var4);
         var6 = factory.getOWLSubClassOfAxiom(var2.getDomain(), var5);
         var1.add(var6);
      } else {
         OWLObjectSomeValuesFrom var11;
         if (var0.getConstraintType() == ConstraintStructure.ConstraintType.OBJECT_SOME_VALUES_FROM) {
            ObjectSomeValuesFromStructure var8 = (ObjectSomeValuesFromStructure)var0;
            var11 = factory.getOWLObjectSomeValuesFrom(var8.getProperty(), var8.getRange());
            var4 = factory.getOWLObjectIntersectionOf(new OWLClassExpression[]{event, var11});
            OWLObjectSomeValuesFrom var21 = factory.getOWLObjectSomeValuesFrom(var8.getProperty(), var4);
            var6 = factory.getOWLSubClassOfAxiom(var8.getDomain(), var21);
            var1.add(var6);
         } else if (var0.getConstraintType() == ConstraintStructure.ConstraintType.OBJECT_MIN_CARDINALITY) {
            ObjectMinCardinalityStructure var9 = (ObjectMinCardinalityStructure)var0;
            var11 = factory.getOWLObjectSomeValuesFrom(var9.getProperty(), var9.getRange());
            var4 = factory.getOWLObjectIntersectionOf(new OWLClassExpression[]{event, var11});
            OWLObjectMinCardinality var23 = factory.getOWLObjectMinCardinality(var9.getCardinality(), var9.getProperty(), var4);
            var6 = factory.getOWLSubClassOfAxiom(var9.getDomain(), var23);
            var1.add(var6);
         } else {
            OWLAnnotation var14;
            OWLAnnotationAssertionAxiom var18;
            if (var0.getConstraintType() == ConstraintStructure.ConstraintType.OBJECT_MAX_CARDINALITY) {
               ObjectMaxCardinalityStructure var10 = (ObjectMaxCardinalityStructure)var0;
               var14 = factory.getOWLAnnotation(superClassConstraint, factory.getOWLLiteral("constraint: ObjectMaxCardinality\ncardinality: " + var10.getCardinality() + "\n" + "property: " + var10.getProperty().getIRI().toString() + "\n" + "range: " + var10.getRange().asOWLClass().getIRI().toString(), "en"));
               var18 = factory.getOWLAnnotationAssertionAxiom(var10.getDomain().getIRI(), var14);
               var1.add(var18);
               var1.add(getTemporalObjectMaxCardinalityRule(var10));
            } else if (var0.getConstraintType() == ConstraintStructure.ConstraintType.OBJECT_EXACT_CARDINALITY) {
               ObjectExactCardinalityStructure var12 = (ObjectExactCardinalityStructure)var0;
               var14 = factory.getOWLAnnotation(superClassConstraint, factory.getOWLLiteral("constraint: ObjectExactCardinality\ncardinality: " + var12.getCardinality() + "\n" + "property: " + var12.getProperty().getIRI().toString() + "\n" + "range: " + var12.getRange().asOWLClass().getIRI().toString(), "en"));
               var18 = factory.getOWLAnnotationAssertionAxiom(var12.getDomain().getIRI(), var14);
               var1.add(var18);
               var1.add(getTemporalObjectExactCardinalityRule(var12));
            } else if (var0.getConstraintType() == ConstraintStructure.ConstraintType.OBJECT_HAS_VALUE) {
               ObjectHasValueStructure var13 = (ObjectHasValueStructure)var0;
               var14 = factory.getOWLAnnotation(superClassConstraint, factory.getOWLLiteral("constraint: ObjectHasValue\nproperty: " + var13.getProperty().getIRI().toString() + "\n" + "range: " + var13.getRange().asOWLNamedIndividual().getIRI().toString(), "en"));
               var18 = factory.getOWLAnnotationAssertionAxiom(var13.getDomain().getIRI(), var14);
               var1.add(var18);
               var1.add(getTemporalObjectHasValueRule(var13));
            } else {
               OWLSubClassOfAxiom var7;
               OWLObjectProperty var19;
               OWLObjectIntersectionOf var26;
               if (var0.getConstraintType() == ConstraintStructure.ConstraintType.DATA_ALL_VALUES_FROM) {
                  DataAllValuesFromStructure var15 = (DataAllValuesFromStructure)var0;
                  var19 = factory.getOWLObjectProperty(":" + Renderer.getName(var15.getProperty()) + "OP", pm);
                  OWLDataAllValuesFrom var25 = factory.getOWLDataAllValuesFrom(var15.getProperty(), var15.getRange());
                  var26 = factory.getOWLObjectIntersectionOf(new OWLClassExpression[]{event, var25});
                  OWLObjectAllValuesFrom var28 = factory.getOWLObjectAllValuesFrom(var19, var26);
                  var7 = factory.getOWLSubClassOfAxiom(var15.getDomain(), var28);
                  var1.add(var7);
               } else {
                  OWLDataSomeValuesFrom var27;
                  if (var0.getConstraintType() == ConstraintStructure.ConstraintType.DATA_SOME_VALUES_FROM) {
                     DataSomeValuesFromStructure var16 = (DataSomeValuesFromStructure)var0;
                     var19 = factory.getOWLObjectProperty(":" + Renderer.getName(var16.getProperty()) + "OP", pm);
                     var27 = factory.getOWLDataSomeValuesFrom(var16.getProperty(), var16.getRange());
                     var26 = factory.getOWLObjectIntersectionOf(new OWLClassExpression[]{event, var27});
                     OWLObjectSomeValuesFrom var29 = factory.getOWLObjectSomeValuesFrom(var19, var26);
                     var7 = factory.getOWLSubClassOfAxiom(var16.getDomain(), var29);
                     var1.add(var7);
                  } else if (var0.getConstraintType() == ConstraintStructure.ConstraintType.DATA_MIN_CARDINALITY) {
                     DataMinCardinalityStructure var17 = (DataMinCardinalityStructure)var0;
                     var19 = factory.getOWLObjectProperty(":" + Renderer.getName(var17.getProperty()) + "OP", pm);
                     var27 = factory.getOWLDataSomeValuesFrom(var17.getProperty(), var17.getRange());
                     var26 = factory.getOWLObjectIntersectionOf(new OWLClassExpression[]{event, var27});
                     OWLObjectMinCardinality var30 = factory.getOWLObjectMinCardinality(var17.getCardinality(), var19, var26);
                     var7 = factory.getOWLSubClassOfAxiom(var17.getDomain(), var30);
                     var1.add(var7);
                  } else if (var0.getConstraintType() == ConstraintStructure.ConstraintType.DATA_MAX_CARDINALITY) {
                     DataMaxCardinalityStructure var20 = (DataMaxCardinalityStructure)var0;
                     var14 = factory.getOWLAnnotation(superClassConstraint, factory.getOWLLiteral("constraint: DataMaxCardinality\ncardinality: " + var20.getCardinality() + "\n" + "property: " + var20.getProperty().getIRI().toString() + "\n" + "range: " + var20.getRange().getIRI().toString(), "en"));
                     var18 = factory.getOWLAnnotationAssertionAxiom(var20.getDomain().getIRI(), var14);
                     var1.add(var18);
                     var1.add(getTemporalDataMaxCardinalityRule(var20));
                  } else if (var0.getConstraintType() == ConstraintStructure.ConstraintType.DATA_EXACT_CARDINALITY) {
                     DataExactCardinalityStructure var22 = (DataExactCardinalityStructure)var0;
                     var14 = factory.getOWLAnnotation(superClassConstraint, factory.getOWLLiteral("constraint: DataExactCardinality\ncardinality: " + var22.getCardinality() + "\n" + "property: " + var22.getProperty().getIRI().toString() + "\n" + "range: " + var22.getRange().getIRI().toString(), "en"));
                     var18 = factory.getOWLAnnotationAssertionAxiom(var22.getDomain().getIRI(), var14);
                     var1.add(var18);
                     var1.add(getTemporalDataExactCardinalityRule(var22));
                  } else if (var0.getConstraintType() == ConstraintStructure.ConstraintType.DATA_HAS_VALUE) {
                     DataHasValueStructure var24 = (DataHasValueStructure)var0;
                     var14 = factory.getOWLAnnotation(superClassConstraint, factory.getOWLLiteral("constraint: DataHasValue\nproperty: " + var24.getProperty().getIRI().toString() + "\n" + "range: " + var24.getRange(), "en"));
                     var18 = factory.getOWLAnnotationAssertionAxiom(var24.getDomain().getIRI(), var14);
                     var1.add(var18);
                     var1.add(getTemporalDataHasValueRule(var24));
                  }
               }
            }
         }
      }

      return var1;
   }

   public static Set getTemporalEquivalentClassConstraint(ConstraintStructure var0) {
      HashSet var1 = new HashSet();
      OWLObjectIntersectionOf var4;
      OWLEquivalentClassesAxiom var6;
      if (var0.getConstraintType() == ConstraintStructure.ConstraintType.OBJECT_ALL_VALUES_FROM) {
         ObjectAllValuesFromStructure var2 = (ObjectAllValuesFromStructure)var0;
         OWLObjectAllValuesFrom var3 = factory.getOWLObjectAllValuesFrom(var2.getProperty(), var2.getRange());
         var4 = factory.getOWLObjectIntersectionOf(new OWLClassExpression[]{event, var3});
         OWLObjectAllValuesFrom var5 = factory.getOWLObjectAllValuesFrom(var2.getProperty(), var4);
         var6 = factory.getOWLEquivalentClassesAxiom(var2.getDomain(), var5);
         var1.add(var6);
      } else {
         OWLObjectSomeValuesFrom var11;
         if (var0.getConstraintType() == ConstraintStructure.ConstraintType.OBJECT_SOME_VALUES_FROM) {
            ObjectSomeValuesFromStructure var8 = (ObjectSomeValuesFromStructure)var0;
            var11 = factory.getOWLObjectSomeValuesFrom(var8.getProperty(), var8.getRange());
            var4 = factory.getOWLObjectIntersectionOf(new OWLClassExpression[]{event, var11});
            OWLObjectSomeValuesFrom var21 = factory.getOWLObjectSomeValuesFrom(var8.getProperty(), var4);
            var6 = factory.getOWLEquivalentClassesAxiom(var8.getDomain(), var21);
            var1.add(var6);
         } else if (var0.getConstraintType() == ConstraintStructure.ConstraintType.OBJECT_MIN_CARDINALITY) {
            ObjectMinCardinalityStructure var9 = (ObjectMinCardinalityStructure)var0;
            var11 = factory.getOWLObjectSomeValuesFrom(var9.getProperty(), var9.getRange());
            var4 = factory.getOWLObjectIntersectionOf(new OWLClassExpression[]{event, var11});
            OWLObjectMinCardinality var23 = factory.getOWLObjectMinCardinality(var9.getCardinality(), var9.getProperty(), var4);
            var6 = factory.getOWLEquivalentClassesAxiom(var9.getDomain(), var23);
            var1.add(var6);
         } else {
            OWLAnnotation var14;
            OWLAnnotationAssertionAxiom var18;
            if (var0.getConstraintType() == ConstraintStructure.ConstraintType.OBJECT_MAX_CARDINALITY) {
               ObjectMaxCardinalityStructure var10 = (ObjectMaxCardinalityStructure)var0;
               var14 = factory.getOWLAnnotation(equivClassConstraint, factory.getOWLLiteral("constraint: ObjectMaxCardinality\ncardinality: " + var10.getCardinality() + "\n" + "property: " + var10.getProperty().getIRI().toString() + "\n" + "range: " + var10.getRange().asOWLClass().getIRI().toString(), "en"));
               var18 = factory.getOWLAnnotationAssertionAxiom(var10.getDomain().getIRI(), var14);
               var1.add(var18);
               var1.add(getTemporalObjectMaxCardinalityRule(var10));
            } else if (var0.getConstraintType() == ConstraintStructure.ConstraintType.OBJECT_EXACT_CARDINALITY) {
               ObjectExactCardinalityStructure var12 = (ObjectExactCardinalityStructure)var0;
               var14 = factory.getOWLAnnotation(equivClassConstraint, factory.getOWLLiteral("constraint: ObjectExactCardinality\ncardinality: " + var12.getCardinality() + "\n" + "property: " + var12.getProperty().getIRI().toString() + "\n" + "range: " + var12.getRange().asOWLClass().getIRI().toString(), "en"));
               var18 = factory.getOWLAnnotationAssertionAxiom(var12.getDomain().getIRI(), var14);
               var1.add(var18);
               var1.add(getTemporalObjectExactCardinalityRule(var12));
            } else if (var0.getConstraintType() == ConstraintStructure.ConstraintType.OBJECT_HAS_VALUE) {
               ObjectHasValueStructure var13 = (ObjectHasValueStructure)var0;
               var14 = factory.getOWLAnnotation(equivClassConstraint, factory.getOWLLiteral("constraint: ObjectHasValue\nproperty: " + var13.getProperty().getIRI().toString() + "\n" + "range: " + var13.getRange().asOWLNamedIndividual().getIRI().toString(), "en"));
               var18 = factory.getOWLAnnotationAssertionAxiom(var13.getDomain().getIRI(), var14);
               var1.add(var18);
               var1.add(getTemporalObjectHasValueRule(var13));
            } else {
               OWLEquivalentClassesAxiom var7;
               OWLObjectProperty var19;
               OWLObjectIntersectionOf var26;
               if (var0.getConstraintType() == ConstraintStructure.ConstraintType.DATA_ALL_VALUES_FROM) {
                  DataAllValuesFromStructure var15 = (DataAllValuesFromStructure)var0;
                  var19 = factory.getOWLObjectProperty(":" + Renderer.getName(var15.getProperty()) + "OP", pm);
                  OWLDataAllValuesFrom var25 = factory.getOWLDataAllValuesFrom(var15.getProperty(), var15.getRange());
                  var26 = factory.getOWLObjectIntersectionOf(new OWLClassExpression[]{event, var25});
                  OWLObjectAllValuesFrom var28 = factory.getOWLObjectAllValuesFrom(var19, var26);
                  var7 = factory.getOWLEquivalentClassesAxiom(var15.getDomain(), var28);
                  var1.add(var7);
               } else {
                  OWLDataSomeValuesFrom var27;
                  if (var0.getConstraintType() == ConstraintStructure.ConstraintType.DATA_SOME_VALUES_FROM) {
                     DataSomeValuesFromStructure var16 = (DataSomeValuesFromStructure)var0;
                     var19 = factory.getOWLObjectProperty(":" + Renderer.getName(var16.getProperty()) + "OP", pm);
                     var27 = factory.getOWLDataSomeValuesFrom(var16.getProperty(), var16.getRange());
                     var26 = factory.getOWLObjectIntersectionOf(new OWLClassExpression[]{event, var27});
                     OWLObjectSomeValuesFrom var29 = factory.getOWLObjectSomeValuesFrom(var19, var26);
                     var7 = factory.getOWLEquivalentClassesAxiom(var16.getDomain(), var29);
                     var1.add(var7);
                  } else if (var0.getConstraintType() == ConstraintStructure.ConstraintType.DATA_MIN_CARDINALITY) {
                     DataMinCardinalityStructure var17 = (DataMinCardinalityStructure)var0;
                     var19 = factory.getOWLObjectProperty(":" + Renderer.getName(var17.getProperty()) + "OP", pm);
                     var27 = factory.getOWLDataSomeValuesFrom(var17.getProperty(), var17.getRange());
                     var26 = factory.getOWLObjectIntersectionOf(new OWLClassExpression[]{event, var27});
                     OWLObjectMinCardinality var30 = factory.getOWLObjectMinCardinality(var17.getCardinality(), var19, var26);
                     var7 = factory.getOWLEquivalentClassesAxiom(var17.getDomain(), var30);
                     var1.add(var7);
                  } else if (var0.getConstraintType() == ConstraintStructure.ConstraintType.DATA_MAX_CARDINALITY) {
                     DataMaxCardinalityStructure var20 = (DataMaxCardinalityStructure)var0;
                     var14 = factory.getOWLAnnotation(equivClassConstraint, factory.getOWLLiteral("constraint: DataMaxCardinality\ncardinality: " + var20.getCardinality() + "\n" + "property: " + var20.getProperty().getIRI().toString() + "\n" + "range: " + var20.getRange().getIRI().toString(), "en"));
                     var18 = factory.getOWLAnnotationAssertionAxiom(var20.getDomain().getIRI(), var14);
                     var1.add(var18);
                     var1.add(getTemporalDataMaxCardinalityRule(var20));
                  } else if (var0.getConstraintType() == ConstraintStructure.ConstraintType.DATA_EXACT_CARDINALITY) {
                     DataExactCardinalityStructure var22 = (DataExactCardinalityStructure)var0;
                     var14 = factory.getOWLAnnotation(equivClassConstraint, factory.getOWLLiteral("constraint: DataExactCardinality\ncardinality: " + var22.getCardinality() + "\n" + "property: " + var22.getProperty().getIRI().toString() + "\n" + "range: " + var22.getRange().getIRI().toString(), "en"));
                     var18 = factory.getOWLAnnotationAssertionAxiom(var22.getDomain().getIRI(), var14);
                     var1.add(var18);
                     var1.add(getTemporalDataExactCardinalityRule(var22));
                  } else if (var0.getConstraintType() == ConstraintStructure.ConstraintType.DATA_HAS_VALUE) {
                     DataHasValueStructure var24 = (DataHasValueStructure)var0;
                     var14 = factory.getOWLAnnotation(equivClassConstraint, factory.getOWLLiteral("constraint: DataHasValue\nproperty: " + var24.getProperty().getIRI().toString() + "\n" + "range: " + var24.getRange(), "en"));
                     var18 = factory.getOWLAnnotationAssertionAxiom(var24.getDomain().getIRI(), var14);
                     var1.add(var18);
                     var1.add(getTemporalDataHasValueRule(var24));
                  }
               }
            }
         }
      }

      return var1;
   }

   public static SWRLRule getTemporalNegativeObjectPropertyAssertionRule(TemporalNegativeObjectPropertyTriple var0) {
      SWRLIndividualArgument var1 = factory.getSWRLIndividualArgument(var0.getSubject());
      SWRLIndividualArgument var2 = factory.getSWRLIndividualArgument(var0.getObject());
      SWRLVariable var3 = factory.getSWRLVariable(IRI.create(baseIRI + "e"));
      SWRLIndividualArgument var4 = factory.getSWRLIndividualArgument(var0.getInterval());
      SWRLVariable var5 = factory.getSWRLVariable(IRI.create(baseIRI + "i"));
      HashSet var6 = new HashSet();
      var6.add(factory.getSWRLObjectPropertyAtom(var0.getObjectProperty(), var1, var3));
      var6.add(factory.getSWRLClassAtom(event, var3));
      var6.add(factory.getSWRLObjectPropertyAtom(var0.getObjectProperty(), var3, var2));
      var6.add(factory.getSWRLObjectPropertyAtom(during, var3, var5));
      var6.add(factory.getSWRLObjectPropertyAtom(overlaps, var5, var4));
      SWRLRule var7 = factory.getSWRLRule(var6, Collections.singleton(factory.getSWRLClassAtom(factory.getOWLNothing(), var1)));
      return var7;
   }

   public static SWRLRule getTemporalNegativeDataPropertyAssertionRule(TemporalNegativeDataPropertyTriple var0) {
      SWRLIndividualArgument var1 = factory.getSWRLIndividualArgument(var0.getSubject());
      SWRLLiteralArgument var2 = factory.getSWRLLiteralArgument(var0.getObject());
      SWRLVariable var3 = factory.getSWRLVariable(IRI.create(baseIRI + "e"));
      SWRLIndividualArgument var4 = factory.getSWRLIndividualArgument(var0.getInterval());
      SWRLVariable var5 = factory.getSWRLVariable(IRI.create(baseIRI + "i"));
      HashSet var6 = new HashSet();
      var6.add(factory.getSWRLObjectPropertyAtom(var0.getObjectProperty(), var1, var3));
      var6.add(factory.getSWRLClassAtom(event, var3));
      var6.add(factory.getSWRLDataPropertyAtom(var0.getDataProperty(), var3, var2));
      var6.add(factory.getSWRLObjectPropertyAtom(during, var3, var5));
      var6.add(factory.getSWRLObjectPropertyAtom(overlaps, var5, var4));
      SWRLRule var7 = factory.getSWRLRule(var6, Collections.singleton(factory.getSWRLClassAtom(factory.getOWLNothing(), var1)));
      return var7;
   }
}
