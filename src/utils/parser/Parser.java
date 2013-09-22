/* Generated By:JavaCC: Do not edit this line. Parser.java */
package utils.parser;

import java.util.List;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;

import distribution.GenotypeAgeCountTrio;
import distribution.ZoneDistribution;
import experiment.scenario.Action;
import experiment.scenario.repeaters.ActionRepeat;
import experiment.scenario.repeaters.CyclicRepeat;
import experiment.scenario.repeaters.ProbabilisticRepeat;
import experiment.scenario.Rule;
import experiment.scenario.repeaters.SingleRepeat;
import experiment.scenario.ZoneCommand;
import experiment.individual.genotype.Genotype;
import statistic.StatisticSettings;
import statistic.StatisticSettings.Subiteration;

public class Parser implements ParserConstants {

//============================================//-----------------------|>> RULE LIST <<|----  static final public List<Rule> ruleList() throws ParseException, Exception {
    List<Rule> rules = new LinkedList<Rule>();
    Rule curRule;
    label_1:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case EVERY:
      case IN:
      case WITH_POSSIBILITY:
        ;
        break;
      default:
        jj_la1[0] = jj_gen;
        break label_1;
      }
      curRule = rule();
         rules.add(curRule);
      jj_consume_token(34);
    }
     {if (true) return rules;}
    throw new Error("Missing return statement in function");
  }

 //^^^^^^^^^^^^^^^^

//=======================| RULE 
  static final public Rule rule() throws ParseException, Exception {
     Rule rule;
    ActionRepeat actionRepeat;
    List<Action> actions;
    int from = Rule.FOREVER_BEFORE,
        to = Rule.FOREVER_AFTER;
    Token token;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case IN:
      jj_consume_token(IN);
      token = jj_consume_token(NUMBER);
             from = Integer.parseInt(token.image);
             actionRepeat =
             new SingleRepeat(from);
             to = from;
      break;
    case EVERY:
    case WITH_POSSIBILITY:
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case EVERY:
        jj_consume_token(EVERY);
        token = jj_consume_token(NUMBER);
               actionRepeat =
              new CyclicRepeat(Float.parseFloat(token.image));
        break;
      case WITH_POSSIBILITY:
        jj_consume_token(WITH_POSSIBILITY);
        token = jj_consume_token(NUMBER);
               actionRepeat =
              new ProbabilisticRepeat(Float.parseFloat(token.image));
        break;
      default:
        jj_la1[1] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case FROM:
        jj_consume_token(FROM);
        token = jj_consume_token(NUMBER);
         from=Integer.parseInt(token.image);
        break;
      default:
        jj_la1[2] = jj_gen;
        ;
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case TO:
        jj_consume_token(TO);
        token = jj_consume_token(NUMBER);
         to=Integer.parseInt(token.image);
        break;
      default:
        jj_la1[3] = jj_gen;
        ;
      }
      break;
    default:
      jj_la1[4] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    actions = actionList();
   {if (true) return new Rule(actionRepeat, actions, from, to);}
    throw new Error("Missing return statement in function");
  }

 //^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

//=======================| ACTION LIST  static final public List<Action> actionList() throws ParseException, Exception {
    List<Action> actions = new LinkedList<Action>();
    Action curAction;
    label_2:
    while (true) {
      curAction = action();
         actions.add(curAction);
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case FOR:
        ;
        break;
      default:
        jj_la1[5] = jj_gen;
        break label_2;
      }
    }
     {if (true) return actions;}
    throw new Error("Missing return statement in function");
  }

 //^^^^^^^^^^^^^^^^^^

//=======================| ACTION  static final public Action action() throws ParseException, Exception {
    List<String> zones = new LinkedList<String>();
    ZoneDistribution zoneDistribution = new ZoneDistribution();
    ZoneCommand command;
    Token tokenG, tokenA, tokenN, token;
    jj_consume_token(FOR);
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case NAME:
      label_3:
      while (true) {
        token = jj_consume_token(NAME);
          zones.add(token.image);
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case NAME:
          ;
          break;
        default:
          jj_la1[6] = jj_gen;
          break label_3;
        }
      }
      break;
    case EACH:
      jj_consume_token(EACH);
      break;
    default:
      jj_la1[7] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    jj_consume_token(IMMIGRATION);
    label_4:
    while (true) {
      tokenG = jj_consume_token(GENOTYPE);
      tokenA = jj_consume_token(NUMBER);
      tokenN = jj_consume_token(NUMBER);
           zoneDistribution.addGenotypeDistribution(
                                new GenotypeAgeCountTrio(
                                        Genotype.getGenotype(tokenG.image),
                                        Integer.parseInt(tokenA.image),
                                        Integer.parseInt(tokenN.image)));
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case 35:
        jj_consume_token(35);
        break;
      default:
        jj_la1[8] = jj_gen;
        ;
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case GENOTYPE:
        ;
        break;
      default:
        jj_la1[9] = jj_gen;
        break label_4;
      }
    }
     {if (true) return new Action(
                    zones,
                    new ZoneCommand(
                                ZoneCommand.Type.ADD_INDIVIDUALS,
                                zoneDistribution));}
    throw new Error("Missing return statement in function");
  }

 //^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

//=====================================================//-----------------------|>> STATISTIC SETTINGS <<|----  static final public StatisticSettings statisticSettings() throws ParseException, Exception {
    StatisticSettings settings = new StatisticSettings();
    label_5:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case AGES:
      case GENOTYPES:
      case WITH_IMMATURES:
      case WITHOUT_IMMATURES:
      case AFTER_MOVE_AND_SCENARIO:
      case AFTER_EVOLUTION:
      case AFTER_REPRODACTION:
      case AFTER_COMPETITION:
      case AFTER_DIEING:
      case AFTER_EACH:
        ;
        break;
      default:
        jj_la1[10] = jj_gen;
        break label_5;
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case AGES:
        jj_consume_token(AGES);
                          settings.setShouldDistinguishAges(true);
        break;
      case GENOTYPES:
        jj_consume_token(GENOTYPES);
                               settings.setShouldDistinguishAges(false);
        break;
      case WITH_IMMATURES:
        jj_consume_token(WITH_IMMATURES);
                                    settings.setShouldDisplayImmatures(true);
        break;
      case WITHOUT_IMMATURES:
        jj_consume_token(WITHOUT_IMMATURES);
                                       settings.setShouldDisplayImmatures(false);
        break;
      case AFTER_MOVE_AND_SCENARIO:
        jj_consume_token(AFTER_MOVE_AND_SCENARIO);
                                             settings.addReportingSubiteration(Subiteration.AFTER_MOVE_AND_SCENARIO.ordinal());
        break;
      case AFTER_EVOLUTION:
        jj_consume_token(AFTER_EVOLUTION);
                                     settings.addReportingSubiteration(Subiteration.AFTER_EVOLUTION.ordinal());
        break;
      case AFTER_REPRODACTION:
        jj_consume_token(AFTER_REPRODACTION);
                                        settings.addReportingSubiteration(Subiteration.AFTER_REPRODACTION.ordinal());
        break;
      case AFTER_COMPETITION:
        jj_consume_token(AFTER_COMPETITION);
                                       settings.addReportingSubiteration(Subiteration.AFTER_COMPETITION.ordinal());
        break;
      case AFTER_DIEING:
        jj_consume_token(AFTER_DIEING);
                                  settings.addReportingSubiteration(Subiteration.AFTER_DIEING.ordinal());
        break;
      case AFTER_EACH:
        jj_consume_token(AFTER_EACH);
                                settings.reportAfterEachSubiteration();
        break;
      default:
        jj_la1[11] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
    }
     {if (true) return settings;}
    throw new Error("Missing return statement in function");
  }

 //^^^^^^^^^^^^^^^^^^^

//=====================================================//-----------------------|>> ZONE DISTRIBUTIONS <<|----  static final public Map<String, ZoneDistribution> zoneDistributions() throws ParseException, Exception {
    Map<String, ZoneDistribution> distributions = new HashMap<String, ZoneDistribution>();
    Token name;
    ZoneDistribution distribution;
    label_6:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case NAME:
        ;
        break;
      default:
        jj_la1[12] = jj_gen;
        break label_6;
      }
      name = jj_consume_token(NAME);
      jj_consume_token(36);
      distribution = zoneDistribution();
                 distributions.put(name.image, distribution);
      jj_consume_token(34);
    }
     {if (true) return distributions;}
    throw new Error("Missing return statement in function");
  }

 //^^^^^^^^^^^^^^^^^^^^^^^^

//=======================| DISTRIBUTION  static final public ZoneDistribution zoneDistribution() throws ParseException, Exception {
    ZoneDistribution zoneDistribution = new ZoneDistribution();
   Token tokenG, tokenA, tokenN, token;
    label_7:
    while (true) {
      tokenG = jj_consume_token(GENOTYPE);
      tokenA = jj_consume_token(NUMBER);
      tokenN = jj_consume_token(NUMBER);
           zoneDistribution.addGenotypeDistribution(
                                new GenotypeAgeCountTrio(
                                      Genotype.getGenotype(tokenG.image),
                                      Integer.parseInt(tokenA.image),
                                      Integer.parseInt(tokenN.image)));
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case 35:
        jj_consume_token(35);
        break;
      default:
        jj_la1[13] = jj_gen;
        ;
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case GENOTYPE:
        ;
        break;
      default:
        jj_la1[14] = jj_gen;
        break label_7;
      }
    }
     {if (true) return zoneDistribution;}
    throw new Error("Missing return statement in function");
  }

  static private boolean jj_initialized_once = false;
  /** Generated Token Manager. */
  static public ParserTokenManager token_source;
  static SimpleCharStream jj_input_stream;
  /** Current token. */
  static public Token token;
  /** Next token. */
  static public Token jj_nt;
  static private int jj_ntk;
  static private int jj_gen;
  static final private int[] jj_la1 = new int[15];
  static private int[] jj_la1_0;
  static private int[] jj_la1_1;
  static {
      jj_la1_init_0();
      jj_la1_init_1();
   }
   private static void jj_la1_init_0() {
      jj_la1_0 = new int[] {0xc80,0x880,0x100,0x200,0xc80,0x1000,0x80000000,0x80002000,0x0,0x4000,0x7fe00000,0x7fe00000,0x80000000,0x0,0x4000,};
   }
   private static void jj_la1_init_1() {
      jj_la1_1 = new int[] {0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x8,0x0,0x0,0x0,0x0,0x8,0x0,};
   }

  /** Constructor with InputStream. */
  public Parser(java.io.InputStream stream) {
     this(stream, null);
  }
  /** Constructor with InputStream and supplied encoding */
  public Parser(java.io.InputStream stream, String encoding) {
    if (jj_initialized_once) {
      System.out.println("ERROR: Second call to constructor of static parser.  ");
      System.out.println("       You must either use ReInit() or set the JavaCC option STATIC to false");
      System.out.println("       during parser generation.");
      throw new Error();
    }
    jj_initialized_once = true;
    try { jj_input_stream = new SimpleCharStream(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source = new ParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 15; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  static public void ReInit(java.io.InputStream stream) {
     ReInit(stream, null);
  }
  /** Reinitialise. */
  static public void ReInit(java.io.InputStream stream, String encoding) {
    try { jj_input_stream.ReInit(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 15; i++) jj_la1[i] = -1;
  }

  /** Constructor. */
  public Parser(java.io.Reader stream) {
    if (jj_initialized_once) {
      System.out.println("ERROR: Second call to constructor of static parser. ");
      System.out.println("       You must either use ReInit() or set the JavaCC option STATIC to false");
      System.out.println("       during parser generation.");
      throw new Error();
    }
    jj_initialized_once = true;
    jj_input_stream = new SimpleCharStream(stream, 1, 1);
    token_source = new ParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 15; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  static public void ReInit(java.io.Reader stream) {
    jj_input_stream.ReInit(stream, 1, 1);
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 15; i++) jj_la1[i] = -1;
  }

  /** Constructor with generated Token Manager. */
  public Parser(ParserTokenManager tm) {
    if (jj_initialized_once) {
      System.out.println("ERROR: Second call to constructor of static parser. ");
      System.out.println("       You must either use ReInit() or set the JavaCC option STATIC to false");
      System.out.println("       during parser generation.");
      throw new Error();
    }
    jj_initialized_once = true;
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 15; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  public void ReInit(ParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 15; i++) jj_la1[i] = -1;
  }

  static private Token jj_consume_token(int kind) throws ParseException {
    Token oldToken;
    if ((oldToken = token).next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    if (token.kind == kind) {
      jj_gen++;
      return token;
    }
    token = oldToken;
    jj_kind = kind;
    throw generateParseException();
  }


/** Get the next Token. */
  static final public Token getNextToken() {
    if (token.next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    jj_gen++;
    return token;
  }

/** Get the specific Token. */
  static final public Token getToken(int index) {
    Token t = token;
    for (int i = 0; i < index; i++) {
      if (t.next != null) t = t.next;
      else t = t.next = token_source.getNextToken();
    }
    return t;
  }

  static private int jj_ntk() {
    if ((jj_nt=token.next) == null)
      return (jj_ntk = (token.next=token_source.getNextToken()).kind);
    else
      return (jj_ntk = jj_nt.kind);
  }

  static private java.util.List<int[]> jj_expentries = new java.util.ArrayList<int[]>();
  static private int[] jj_expentry;
  static private int jj_kind = -1;

  /** Generate ParseException. */
  static public ParseException generateParseException() {
    jj_expentries.clear();
    boolean[] la1tokens = new boolean[37];
    if (jj_kind >= 0) {
      la1tokens[jj_kind] = true;
      jj_kind = -1;
    }
    for (int i = 0; i < 15; i++) {
      if (jj_la1[i] == jj_gen) {
        for (int j = 0; j < 32; j++) {
          if ((jj_la1_0[i] & (1<<j)) != 0) {
            la1tokens[j] = true;
          }
          if ((jj_la1_1[i] & (1<<j)) != 0) {
            la1tokens[32+j] = true;
          }
        }
      }
    }
    for (int i = 0; i < 37; i++) {
      if (la1tokens[i]) {
        jj_expentry = new int[1];
        jj_expentry[0] = i;
        jj_expentries.add(jj_expentry);
      }
    }
    int[][] exptokseq = new int[jj_expentries.size()][];
    for (int i = 0; i < jj_expentries.size(); i++) {
      exptokseq[i] = jj_expentries.get(i);
    }
    return new ParseException(token, exptokseq, tokenImage);
  }

  /** Enable tracing. */
  static final public void enable_tracing() {
  }

  /** Disable tracing. */
  static final public void disable_tracing() {
  }

}
