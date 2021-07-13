package structures;

import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;

public class TemporalNegativeObjectPropertyTriple extends TemporalPropertyTriple {
   private OWLNamedIndividual range;

   public TemporalNegativeObjectPropertyTriple(OWLNamedIndividual var1, OWLNamedIndividual var2, OWLNamedIndividual var3, OWLObjectProperty var4) {
      this.domain = var1;
      this.range = var2;
      this.interval = var3;
      this.op = var4;
   }

   public TemporalNegativeObjectPropertyTriple() {
      this.range = null;
   }

   public void setObject(OWLNamedIndividual var1) {
      this.range = var1;
   }

   public OWLNamedIndividual getObject() {
      return this.range;
   }

   public boolean isTemporal() {
      return this.interval != null;
   }

   public boolean isObjectPropertyTriple() {
      return true;
   }

   public boolean isNegative() {
      return true;
   }
}
