package structures;

import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;

public class TemporalObjectPropertyTriple extends TemporalPropertyTriple {
   private OWLNamedIndividual range;

   public TemporalObjectPropertyTriple(OWLNamedIndividual var1, OWLNamedIndividual var2, OWLNamedIndividual var3, OWLNamedIndividual var4, OWLObjectProperty var5) {
      this.domain = var1;
      this.eventInd = var2;
      this.range = var3;
      this.interval = var4;
      this.op = var5;
   }

   public TemporalObjectPropertyTriple() {
      this.range = null;
   }

   public void setObject(OWLNamedIndividual var1) {
      this.range = var1;
   }

   public OWLNamedIndividual getObject() {
      return this.range;
   }

   public boolean isTemporal() {
      return this.eventInd != null && this.interval != null;
   }

   public boolean isObjectPropertyTriple() {
      return true;
   }

   public boolean isNegative() {
      return false;
   }
}
