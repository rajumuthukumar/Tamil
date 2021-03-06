package my.interest.lang.tamil.impl;

import my.interest.lang.tamil.TamilUtils;
import my.interest.lang.tamil.generated.types.*;
import my.interest.lang.tamil.impl.job.ExecuteManager;
import my.interest.lang.tamil.internal.api.DefinitionFactory;
import my.interest.lang.tamil.internal.api.PersistenceInterface;

import my.interest.lang.tamil.multi.WordGeneratorFromIdai;
import my.interest.lang.tamil.multi.WordGeneratorFromPeyar;
import my.interest.lang.tamil.multi.WordGeneratorFromVinaiyadi;
import my.interest.lang.tamil.punar.handler.verrrrumai.VAllHandler;
import my.interest.lang.tamil.xml.AppCache;
import tamil.lang.api.persist.manager.ApplicationResourceManager;
import tamil.lang.exception.service.ServiceException;
import tamil.lang.known.non.derived.idai.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.*;
import java.util.logging.Logger;

/**
 * <p>
 * </p>
 *
 * @author velsubra
 */
public class FileBasedPersistence extends PersistenceInterface   {
    static final Logger logger = Logger.getLogger(FileBasedPersistence.class.getName());
    private String path = null;

    public static PersistenceInterface ME_SINGLETON = new FileBasedPersistence();


    static TamilRootWords cached = null;
    static String lastloaded = null;

    static void populateSorted() {
        roots = new TreeSet<RootVerbDescription>(new Comparator<RootVerbDescription>() {
            @Override
            public int compare(RootVerbDescription o1, RootVerbDescription o2) {
                return o1.getRoot().compareTo(o2.getRoot());
            }
        });

        peyars = new TreeSet<PeyarchcholDescription>(new Comparator<PeyarchcholDescription>() {
            @Override
            public int compare(PeyarchcholDescription o1, PeyarchcholDescription o2) {
                return o1.getRoot().compareTo(o2.getRoot());
            }
        });

        idais = new TreeSet<IdaichcholDescription>(new Comparator<IdaichcholDescription>() {
            @Override
            public int compare(IdaichcholDescription o1, IdaichcholDescription o2) {
                return o1.getRoot().compareTo(o2.getRoot());
            }
        });

        if (cached.getApps() == null) {
            cached.setApps(new Apps());

        }

        if (cached.getApps().getApps() == null) {
            cached.getApps().setApps(new AppsDescription());
        }
        if (cached.getApps().getApps().getList() == null) {
            cached.getApps().getApps().setList(new AppsDescriptionList());
        }


        if (cached.getVinai() == null) {
            cached.setVinai(new Vinai());
        }
        if (cached.getVinai().getVerbs() == null) {
            cached.getVinai().setVerbs(new RootVerbsDescription());
        }

        if (cached.getVinai().getVerbs().getList() == null) {
            cached.getVinai().getVerbs().setList(new RootVerbsList());
        }
        if (cached.getPeyar() == null) {
            cached.setPeyar(new Peyar());
        }
        if (cached.getPeyar().getWords() == null) {
            cached.getPeyar().setWords(new PeyarchchchorrkalhDescription());
        }

        if (cached.getPeyar().getWords().getList() == null) {
            cached.getPeyar().getWords().setList(new PeyarchchchorrkalhList());
        }


        if (cached.getIdai() == null) {
            cached.setIdai(new Idai());
        }
        if (cached.getIdai().getWords() == null) {
            cached.getIdai().setWords(new IdaichchorrkalhDescription());
        }

        if (cached.getIdai().getWords().getList() == null) {
            cached.getIdai().getWords().setList(new IdaichchorrkalhList());
        }



        peyars.addAll(cached.getPeyar().getWords().getList().getWord());
        idais.addAll(cached.getIdai().getWords().getList().getWord());
        roots.addAll(cached.getVinai().getVerbs().getList().getVerb());


        //List<RootVerbDescription> kuttu = new ArrayList<RootVerbDescription>();
//        for (RootVerbDescription root : cached.getVinai().getVerbs().getList().getVerb()) {
//
//            TamilWord currentRoot = TamilWord.from(root.getRoot());
//            if (!currentRoot.endsWith(iduword, false) && new TamilWordPartContainer(currentRoot).isUkkurralh()) {
//                kuttu.add(root);
//            }
//
//        }
//        RootVerbDescription idu = PersistenceInterface.get().findRootVerbDescription(iduword.toString());
//        //வணங்கிடு
//        for (RootVerbDescription k : kuttu)  {
//            TamilWord currentRoot = TamilWord.from(k.getRoot());
//            WordsJoiner joiner = TamilFactory.createWordJoiner(currentRoot);
//            joiner.addVaruMozhi(iduword);
//            currentRoot = joiner.getSum();
//
//            RootVerbDescription _idu = new RootVerbDescription();
//            _idu.setRoot(currentRoot.toString());
//            _idu.setDescription(idu.getDescription());
//           // cached.getVinai().getVerbs().getList().getVerb().add(_idu);
//            roots.add(_idu);
//        }




        reCompileAllScripts();


    }

    private static void reCompileAllScripts() {
        for (AppDescription app : cached.getApps().getApps().getList().getApp()) {
            if (app.getCache() == null)  {
                app.setCache(new AppCache());
            }
            if (app.getCache().getAppClassLoader() == null) {
                try {
                    app.getCache().buildClassloader(app);
                } catch (Exception e) {
                    e.printStackTrace();
                    //ignore
                }
            }

            if (app.getResources() == null) {
                app.setResources(new AppResources());

            }
            for (AppResource res : app.getResources().getResources()) {
                if (AppResourceType.GROOVY == res.getType()) {
                    try {
                        compile(app,res, app.getName() + "." + res.getName(), URLDecoder.decode(new String(res.getContent()), "UTF-8"), true);
                    } catch (Exception e) {
                        e.printStackTrace();
                        //ignore
                    }
                }
            }

        }
    }

    private FileBasedPersistence() {
        this.path = new File(getWorkDir(), "i18n.xml").getAbsolutePath();
        if (lastloaded == null) {
            lastloaded = getLastModified();
        }
    }

    private String getLastModified() {
        try {
            File f = new File(new File(path).getParent(), "lastmodified.txt");
            if (!f.exists()) {
                setLastModified();
            }
            return new String(TamilUtils.readAllFromFile(f.getAbsolutePath()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void setLastModified() {
        try {
            File f = new File(new File(path).getParent(), "lastmodified.txt");

            TamilUtils.writeToFile(f, String.valueOf(new Date().getTime()).getBytes());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public TamilRootWords getAllRootWords() {
        File file = new File(path);
        if (!file.exists()) {
            InputStream in = DefinitionFactory.class.getResourceAsStream("/data/tamil-root-words.xml");
            try {
                TamilUtils.writeToFile(file, in);
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                try {
                    in.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }

        if (cached != null) {
            if (lastloaded.equals(getLastModified())) {
                return cached;
            }
        }
        lastloaded = getLastModified();
        FileInputStream fis = null;
        boolean alreadylocked = isLockedByCurrentThread();
        try {
            if (!alreadylocked) {
                lock();
            }
            logger.info("xml loaded ...");
            fis = new FileInputStream(file);
            JAXBContext jc = JAXBContext.newInstance(TamilRootWords.class);
            Unmarshaller unmarshaller = jc.createUnmarshaller();
            cached = (TamilRootWords) unmarshaller.unmarshal(fis);
            populateSorted();
//            System.out.println("Verbs:" + cached.getVinai().getVerbs().getList().getVerb().size());
//            System.out.println("idai:" + cached.getIdai().getWords().getList().getWord().size());
//            System.out.println("peyar:" + cached.getPeyar().getWords().getList().getWord().size());


            if (isEmptyKnown() && autoLoad) {

                trim(cached.getPeyar().getGlobalTypes());
                trim(cached.getIdai().getGlobalTypes());
                trim(cached.getVinai().getGlobalTypes());



                ExecuteManager.fire(new Runnable() {

                    @Override
                    public void run() {

                        for (RootVerbDescription root : roots) {

                            // System.out.print(count++ + ":" + root.getRoot() + ":");
                            ExecuteManager.fire(new WordGeneratorFromVinaiyadi(root));
                            // System.out.println(set.size());


                        }

                    }
                });

                for (VUrubu u: VAllHandler.all.keySet()) {
                    addKnown(u);
                }


                addKnown( Kalh.KALH);
                addKnown(Thaan.THAAN);
                addKnown(Um.UM);
                addKnown(Aththu.ATHTHU);
                addKnown(Ottu.IK);
                addKnown(Ottu.ICH);
                addKnown(Ottu.ITH);
                addKnown(Ottu.IP);
                addKnown(Aaga.AAGA);
                addKnown(Aay.AAY);
                addKnown(Aa.AA);
                addKnown(Ea.EA);
                addKnown(Oa.OA);
                addKnown(new Kaaran());
                addKnown(new Kaarar());
                //addKnown(new Maaddu());

//                addKnown(new Ottu("ந்"));
//                addKnown(new Ottu("ஞ்"));
//                addKnown(new Ottu("ம்"));
//                addKnown(new Ottu("வ்"));


                final FileBasedPersistence per = this;


                ExecuteManager.fire(new Runnable() {

                    @Override
                    public void run() {
                        for (IdaichcholDescription idai : cached.getIdai().getWords().getList().getWord()) {

                            ExecuteManager.fire(new WordGeneratorFromIdai(idai, per));

                        }
                    }
                });



                ExecuteManager.fire(new Runnable() {

                    @Override
                    public void run() {
                        for (PeyarchcholDescription peyar : cached.getPeyar().getWords().getList().getWord()) {

                            ExecuteManager.fire(new WordGeneratorFromPeyar(peyar, per));

                        }
                    }
                });

            }

            return cached;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {

            if (fis != null) {
                try {
                    fis.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (!alreadylocked) {
                unlock();
            }

        }

    }

    private void trim(GlobalTypes types) {
        if (types == null) return;
        for (Typed t : types.getDeclare()) {
            t.setName(t.getName().trim());

        }
    }


    @Override
    public void persist(TamilRootWords verbs) {
        try {
            setLastModified();
            cached = verbs;

            lastloaded = getLastModified();
            File file = new File(path);
            logger.info("xml persisted ...");
            JAXBContext jc = JAXBContext.newInstance(TamilRootWords.class);
            Marshaller marshaller = jc.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(verbs, file);


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void lock() {
        try {
            if (isLockedByCurrentThread()) return;
            new File(path).setLastModified(new Date().getTime());
            File file = new File(path + ".lock");
            int count = 20;
            while (file.exists()) {

                Thread.currentThread().sleep(1000);
                count--;
                if (count == 0) {
                    throw new Exception("Cloud not lock for updates.");
                }

            }
            if (!file.createNewFile()) {
                throw new Exception("Cloud not lock for updates.");
            }
            logger.info("xml locked ...");
            TamilUtils.writeToFile(file, Thread.currentThread().getName().getBytes());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void unlock() {
        try {
            File file = new File(path + ".lock");

            if (isLockedByCurrentThread()) {
                if (!file.delete()) {
                    throw new Exception("Cloud not unlock.");
                }
            }
            logger.info("xml unlocked ...");
        } catch (Exception e) {
            throw new RuntimeException("Could not unlock.", e);
        }
    }

    private boolean isLockedByCurrentThread() {
        try {
            File file = new File(path + ".lock");
            if (!file.exists()) {
                return false;
            }
            byte[] data = TamilUtils.readAllFromFile(file.getAbsolutePath());
            return new String(data, TamilUtils.ENCODING).equals(Thread.currentThread().getName());
        } catch (Exception e) {
            throw new RuntimeException("Unable to check ...", e);
        }
    }


    @Override
    public GlobalTypes getNounGlobalTypes() {
        return getAllRootWords().getPeyar().getGlobalTypes();
    }

    public AppDescription findAppByName(String name) {
        TamilRootWords file = getAllRootWords();

        return findApp(name,file,false);
    }

    public AppDescription createAppByName(String name) throws ServiceException {
         createApp(null,name);
        return  findAppByName(name);
    }

    public AppDescription readApplicationByName(String name) {
        return null;
    }

    public void deleteApplicationByName(String name) {
        deleteApp(null,name);
    }

    public void writeApplication(AppDescription app, boolean overWrite) {

    }

    public ApplicationResourceManager getResourceManager(String appName) {
        return null;
    }

    public void updateApplication(AppDescription app) {

    }
}
