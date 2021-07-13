package ui;

import org.semanticweb.owlapi.model.OWLEntity;

public class Renderer {

    public Renderer() {
    }

    public static String getName(OWLEntity oe) {
        if (oe.getIRI().getFragment() != null) {
            return oe.getIRI().getFragment();
        } else {
            String iri = oe.getIRI().toString();
            int a = iri.lastIndexOf(47);
            return iri.substring(a + 1);
        }
    }
}
