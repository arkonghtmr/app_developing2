package ru.mirea.fedorov.dogguide.data.ml;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class DogCeoLabelMapper {
    private final Map<String, String> overrides = new HashMap<>();
    private final Set<String> dogLabels = new HashSet<>();

    public DogCeoLabelMapper() {
        put("chihuahua", "chihuahua");
        put("japanese spaniel", "spaniel/japanese");
        put("maltese dog", "maltese");
        put("pekinese", "pekinese");
        put("shih-tzu", "shihtzu");
        put("blenheim spaniel", "spaniel/blenheim");
        put("papillon", "papillon");
        put("afghan hound", "hound/afghan");
        put("basset", "hound/basset");
        put("beagle", "beagle");
        put("bloodhound", "hound/blood");
        put("bluetick", "bluetick");
        put("walker hound", "hound/walker");
        put("english foxhound", "hound/english");
        put("ibizan hound", "hound/ibizan");
        put("norwegian elkhound", "elkhound");
        put("otterhound", "otterhound");
        put("saluki", "saluki");
        put("scottish deerhound", "deerhound/scottish");
        put("weimaraner", "weimaraner");
        put("staffordshire bullterrier", "staffordshire");
        put("american staffordshire terrier", "terrier/american");
        put("bedlington terrier", "terrier/bedlington");
        put("border terrier", "terrier/border");
        put("kerry blue terrier", "terrier/kerryblue");
        put("irish terrier", "terrier/irish");
        put("norfolk terrier", "terrier/norfolk");
        put("norwich terrier", "terrier/norwich");
        put("yorkshire terrier", "terrier/yorkshire");
        put("wire-haired fox terrier", "terrier/fox");
        put("lakeland terrier", "terrier/lakeland");
        put("sealyham terrier", "terrier/sealyham");
        put("airedale", "airedale");
        put("cairn", "cairn");
        put("australian terrier", "terrier/australian");
        put("dandie dinmont", "terrier/dandie");
        put("boston bull", "bulldog/boston");
        put("miniature schnauzer", "schnauzer/miniature");
        put("giant schnauzer", "schnauzer/giant");
        put("west highland white terrier", "terrier/westhighland");
        put("lhasa", "lhasa");
        put("flat-coated retriever", "retriever/flatcoated");
        put("curly-coated retriever", "retriever/curly");
        put("golden retriever", "retriever/golden");
        put("labrador retriever", "labrador");
        put("chesapeake bay retriever", "retriever/chesapeake");
        put("german short-haired pointer", "pointer/german");
        put("vizsla", "vizsla");
        put("english setter", "setter/english");
        put("irish setter", "setter/irish");
        put("gordon setter", "setter/gordon");
        put("brittany spaniel", "spaniel/brittany");
        put("clumber", "clumber");
        put("english springer", "spaniel/english");
        put("welsh springer spaniel", "spaniel/welsh");
        put("cocker spaniel", "spaniel/cocker");
        put("sussex spaniel", "spaniel/sussex");
        put("irish water spaniel", "spaniel/irish");
        put("kuvasz", "kuvasz");
        put("schipperke", "schipperke");
        put("groenendael", "groenendael");
        put("malinois", "malinois");
        put("briard", "briard");
        put("kelpie", "kelpie");
        put("komondor", "komondor");
        put("old english sheepdog", "sheepdog/english");
        put("shetland sheepdog", "sheepdog/shetland");
        put("collie", "collie");
        put("border collie", "collie/border");
        put("bouvier des flandres", "bouvier");
        put("rottweiler", "rottweiler");
        put("german shepherd", "germanshepherd");
        put("doberman", "doberman");
        put("miniature pinscher", "pinscher/miniature");
        put("greater swiss mountain dog", "mountain/swiss");
        put("bernese mountain dog", "mountain/bernese");
        put("appenzeller", "appenzeller");
        put("entlebucher", "entlebucher");
        put("boxer", "boxer");
        put("bull mastiff", "mastiff/bull");
        put("tibetan mastiff", "mastiff/tibetan");
        put("french bulldog", "bulldog/french");
        put("great dane", "dane/great");
        put("saint bernard", "stbernard");
        put("eskimo dog", "husky");
        put("malamute", "malamute");
        put("siberian husky", "husky");
        put("dalmatian", "dalmatian");
        put("affenpinscher", "affenpinscher");
        put("basenji", "basenji");
        put("pug", "pug");
        put("leonberg", "leonberg");
        put("newfoundland", "newfoundland");
        put("great pyrenees", "pyrenees");
        put("samoyed", "samoyed");
        put("pomeranian", "pomeranian");
        put("chow", "chow");
        put("keeshond", "keeshond");
        put("pembroke", "pembroke");
        put("cardigan", "corgi/cardigan");
        put("toy poodle", "poodle/toy");
        put("miniature poodle", "poodle/miniature");
        put("standard poodle", "poodle/standard");
        put("mexican hairless", "mexicanhairless");
        put("dingo", "dingo");
        put("dhole", "dhole");
        put("african hunting dog", "african");
        put("redbone", "redbone");
        put("borzoi", "borzoi");
        put("whippet", "whippet");
        put("rhodesian ridgeback", "ridgeback");
        put("standard schnauzer", "schnauzer/miniature");
        put("scotch terrier", "terrier/scottish");
        put("tibetan terrier", "terrier/tibetan");
        put("silky terrier", "terrier/silky");
        put("soft-coated wheaten terrier", "terrier/wheaten");
        put("toy terrier", "terrier/toy");
        put("irish wolfhound", "wolfhound/irish");
        put("italian greyhound", "greyhound/italian");
        put("brabancon griffon", "brabancon");
    }

    public boolean isDog(String label) {
        return dogLabels.contains(normalize(label));
    }

    public String toBreedId(String label) {
        String key = normalize(label);
        String mapped = overrides.get(key);
        if (mapped != null) {
            return mapped;
        }
        String[] parts = key.split("\\s+");
        if (parts.length >= 2) {
            String last = parts[parts.length - 1];
            if (last.equals("retriever") || last.equals("hound") || last.equals("spaniel")
                    || last.equals("terrier") || last.equals("poodle") || last.equals("setter")
                    || last.equals("mastiff") || last.equals("pinscher") || last.equals("collie")
                    || last.equals("sheepdog")) {
                return last + "/" + parts[0];
            }
        }
        return parts[parts.length - 1];
    }

    private void put(String label, String breedId) {
        String key = normalize(label);
        overrides.put(key, breedId);
        dogLabels.add(key);
    }

    private String normalize(String label) {
        return label.toLowerCase(Locale.ROOT).trim();
    }
}
