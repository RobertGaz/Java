package structures;

import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;

public abstract class TemporalPropertyTriple {
   OWLNamedIndividual domain = null;
   OWLNamedIndividual eventInd = null;
   OWLNamedIndividual interval = null;
   OWLObjectProperty op = null;

   public void setSubject(OWLNamedIndividual var1) {
      this.domain = var1;
   }

   public void setEvent(OWLNamedIndividual var1) {
      this.eventInd = var1;
   }

   public void setObjectProperty(OWLObjectProperty var1) {
      this.op = var1;
   }

   public void setInterval(OWLNamedIndividual var1) {
      this.interval = var1;
   }

   public OWLNamedIndividual getSubject() {
      return this.domain;
   }

   public OWLNamedIndividual getEvent() {
      return this.eventInd;
   }

   public OWLObjectProperty getObjectProperty() {
      return this.op;
   }

   public OWLNamedIndividual getInterval() {
      return this.interval;
   }

   public abstract boolean isTemporal();

   public abstract boolean isObjectPropertyTriple();

   public abstract boolean isNegative();
}
