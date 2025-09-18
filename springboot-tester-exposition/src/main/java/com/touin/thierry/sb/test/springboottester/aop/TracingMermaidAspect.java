package com.touin.thierry.sb.test.springboottester.aop;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

@Aspect
@Component
public class TracingMermaidAspect {

    private static final String[] EXCLUDED_PACKAGES = {
        "com.touin.thierry.sb.test.springboottester.infrastructure.config"
    };


    @Around("within(com.touin.thierry.sb..*)")
    public Object trace(ProceedingJoinPoint pjp) throws Throwable {

        String typeName = pjp.getSignature().getDeclaringTypeName();

        // Vérifier si le package courant est exclu
        if (isExcluded(typeName)) {
            return pjp.proceed(); // ne pas tracer, exécuter normalement
        }

        String fqcn = pjp.getSignature().getDeclaringTypeName() + "." + pjp.getSignature().getName();
        String module = determineModuleFromPackage(typeName);
        trace.get().add(module + ": " + fqcn);

        Object result = pjp.proceed();

        if (isController(pjp)) {
            String mermaid = buildMermaid(trace.get());
            System.out.println("\n```mermaid\n" + mermaid + "\n```\n");

            String mermaidSeq = buildMermaidSequence(trace.get());
            System.out.println("\n```mermaid\n" + mermaidSeq + "\n```\n");

            trace.remove();
        }

        return result;
    }

    private boolean isExcluded(String fullyQualifiedClassName) {
        if (EXCLUDED_PACKAGES == null || EXCLUDED_PACKAGES.length == 0) {
            return false;
        }

        for (String excludedPackage : EXCLUDED_PACKAGES) {
            if (fullyQualifiedClassName.startsWith(excludedPackage)) {
                return true;
            }
        }
        return false;
    }

    private static String determineModuleFromPackage(String declaringTypeName) {
        // declaringTypeName est typiquement "com.foo.bar.MyClass"
        String pkg = packageOf(declaringTypeName);
        String n = pkg.toLowerCase(Locale.ROOT);

        // --- mapping principal (hexagonal) ---
        if (n.contains(".exposition.") || n.endsWith(".exposition")) {
            return "exposition";
        }

        if (n.contains(".application.") || n.endsWith(".application")) {
            return "application";
        }

        if (n.contains(".domain.") || n.endsWith(".domain")) {
            return "domain";
        }

        if (n.contains(".infrastructure.") || n.endsWith(".infrastructure")) {
            return "infrastructure";
        }

        return "unknown";
    }

    private static String packageOf(String declaringTypeName) {
        int lastDot = declaringTypeName.lastIndexOf('.');
        return (lastDot > 0) ? declaringTypeName.substring(0, lastDot) : "";
    }

    private static final ThreadLocal<List<String>> trace = ThreadLocal.withInitial(ArrayList::new);

    // (determineModuleFromPackage + packageOf identiques à ta version)

    private String buildMermaid(List<String> nodes) {
        StringBuilder sb = new StringBuilder();
        sb.append("flowchart TD\n");
        sb.append("  classDef exposition fill:#FFD700,stroke:#333,stroke-width:2px;\n");
        sb.append("  classDef application fill:#1E90FF,stroke:#333,stroke-width:2px;\n");
        sb.append("  classDef domain fill:#32CD32,stroke:#333,stroke-width:2px;\n");
        sb.append("  classDef infrastructure fill:#FF69B4,stroke:#333,stroke-width:2px;\n\n");

        // Déclarer chaque nœud une fois avec ID unique
        for (int i = 0; i < nodes.size(); i++) {
            String node = nodes.get(i);
            String id = "N" + i;
            sb.append(String.format("  %s[\"%s\"]\n", id, node));
            String module = node.substring(0, node.indexOf(":"));
            sb.append(String.format("  class %s %s;\n", id, module));
        }
        sb.append("\n");

        // Déclarer les flèches entre les IDs
        for (int i = 0; i < nodes.size() - 1; i++) {
            sb.append(String.format("  N%d --> N%d\n", i, i + 1));
        }

        // Optionnel : style global pour les liens
        sb.append("\n  linkStyle default stroke:#555,stroke-width:1px;\n");
        return sb.toString();
    }

    private boolean isController(ProceedingJoinPoint pjp) {
        return pjp.getSignature().getDeclaringType().isAnnotationPresent(RestController.class);
    }

    /**
     * Génère un diagramme de séquence Mermaid à partir de la trace (liste "module:
     * fqcn.method").
     * - Affiche Class.method (sans package)
     * - Regroupe les messages par module source et met un rect coloré par groupe
     */
    private String buildMermaidSequence(List<String> nodes) {
        if (nodes == null || nodes.size() < 2) {
            return "sequenceDiagram\n  autonumber\n  participant E as Exposition\n  participant A as Application\n  participant D as Domain\n  participant I as Infrastructure\n";
        }

        // Couleurs (rgba pour fond semi-transparent)
        Map<String, String> colorMap = Map.of(
                "E", "rgba(255,215,0,0.12)",
                "A", "rgba(30,144,255,0.12)",
                "D", "rgba(50,205,50,0.12)",
                "I", "rgba(255,105,180,0.12)",
                "unknown", "rgba(200,200,200,0.08)");

        StringBuilder sb = new StringBuilder();
        // Init optionnel (taille / style des acteurs/messages)
        sb.append(
                "%%{init: {'sequence': {'actorFontSize':14,'actorFontWeight':600,'messageFontSize':12,'rightAngles':true}}}%%\n");
        sb.append("sequenceDiagram\n");
        sb.append("  autonumber\n");
        sb.append("  participant E as Exposition\n");
        sb.append("  participant A as Application\n");
        sb.append("  participant D as Domain\n");
        sb.append("  participant I as Infrastructure\n\n");

        System.out.println("\n```nodes\n" + nodes + "\n```\n");

        // On parcourt les arêtes (i -> i+1) et on regroupe les messages
        String currentRectModule = null;
        for (int i = 0; i < nodes.size() - 1; i++) {

            String src = nodes.get(i);
            String tgt = nodes.get(i + 1);

            System.out.println("\n`src : " + src + "`");
            System.out.println("\n`tgt : " + tgt + "`");


            String srcModule = moduleOf(src);
            String tgtModule = moduleOf(tgt);
            System.out.println("\n`srcModule : " + srcModule + "`");
            System.out.println("\n`tgtModule : " + tgtModule + "`");

            String srcActor = actorCode(srcModule); // E/A/D/I
            String tgtActor = actorCode(tgtModule);
            System.out.println("\n`srcActor : " + srcActor + "`");
            System.out.println("\n`tgtActor : " + tgtActor + "`");

            // libellé : Class.method (sans package)
            String label = simpleClassDotMethod(tgt);
            System.out.println("\n`label : " + label + "`");

            srcActor = modifierSrcActor(srcActor, tgtActor);
            System.out.println("\n`srcActor modifié : " + srcActor + "`");

            // démarrer/fermer rect quand le module source change (regroupement)
            if (currentRectModule == null || !currentRectModule.equals(srcModule)) {
                if (currentRectModule != null) {
                    sb.append("  end\n\n"); // fermer précédent
                }
                String color = colorMap.getOrDefault(srcActor, colorMap.get("unknown"));
                sb.append(String.format("  rect %s\n", color));
                currentRectModule = srcModule;
            }

            sb.append(String.format("    %s->>%s: %s\n", srcActor, tgtActor, label));
        }

        if (currentRectModule != null) {
            sb.append("  end\n"); // fermer dernier rect
        }

        return sb.toString();
    }

    public String modifierSrcActor(String srcActor, String tgtActor) {
        // Si égaux, on ne change rien
        if (tgtActor == srcActor) {
            return srcActor;
        }
        
        // Sinon, on applique les règles
        switch (tgtActor) {
            case "I": return "D";
            case "D": return "A";
            case "A": return "E";
            case "E": 
            default: return srcActor; // Pas de changement pour 'E' ou valeurs invalides
        }
    }

    /**
     * Retourne "exposition" / "application" / "domain" / "infrastructure" à partir
     * d'un node "module: fqcn.method"
     */
    private String moduleOf(String node) {
        if (node == null)
            return "unknown";
        int colon = node.indexOf(':');
        if (colon < 0)
            return "unknown";
        return node.substring(0, colon).trim();
    }

    /**
     * Mappe module -> participant code utilisé dans le sequenceDiagram (E/A/D/I)
     */
    private String actorCode(String module) {
        switch (module) {
            case "exposition":
                return "E";
            case "application":
                return "A";
            case "domain":
                return "D";
            case "infrastructure":
                return "I";
            default:
                return "U";
        }
    }

    /**
     * Extrait "Class.method" à partir de "module: com.foo.BarClass.methodName"
     * Si la chaîne n'a pas le format attendu, renvoie la partie après ': ' brute.
     */
    private String simpleClassDotMethod(String node) {
        if (node == null)
            return "";
        int colon = node.indexOf(':');
        String after = (colon >= 0) ? node.substring(colon + 1).trim() : node.trim();
        // after = "com.foo.BarClass.method"
        int lastDot = after.lastIndexOf('.');
        if (lastDot <= 0)
            return after;
        String fqcn = after.substring(0, lastDot); // "com.foo.BarClass"
        String method = after.substring(lastDot + 1); // "method"
        int classDot = fqcn.lastIndexOf('.');
        String classSimple = (classDot >= 0) ? fqcn.substring(classDot + 1) : fqcn;
        return classSimple + "." + method;
    }

}
