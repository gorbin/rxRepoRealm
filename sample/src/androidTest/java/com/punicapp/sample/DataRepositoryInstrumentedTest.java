package com.punicapp.sample;

import android.util.Log;

import com.google.common.base.Optional;
import com.punicapp.rxrepocore.Check;
import com.punicapp.rxrepocore.IRepository;
import com.punicapp.rxrepocore.LocalFilter;
import com.punicapp.rxrepocore.LocalFilters;
import com.punicapp.rxreporealm.DataRepository;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(Parameterized.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DataRepositoryInstrumentedTest {

    private static final int PARAMS_COUNT = 4;
    private static final int TESTS_COUNT = 5;

    private IRepository<TestModel> testRepo = new DataRepository<>(TestModel.class);

    @Parameterized.Parameter(value = 0)
    public TestModel actualValue;

    @Parameterized.Parameter(value = 1)
    public TestModel expectedValue;

    @Parameterized.Parameter(value = 2)
    public LocalFilter localFilterValue;

    @Parameterized.Parameter(value = 3)
    public List<TestModel> dataList;

    @Parameterized.Parameters
    public static Collection<Object[]> initParameters() {
        Object[][] testData = new Object[TESTS_COUNT][PARAMS_COUNT];

        for (int i = 0; i < TESTS_COUNT; i++) {
            testData[i][0] = new TestModel(i);
            testData[i][1] = new TestModel(i);
            switch (i) {
                case 0:
                    testData[i][2] = new LocalFilter("id", Check.In, new Integer[]{i});
                    break;
                case 1:
                    testData[i][2] = new LocalFilter("name", Check.In, new String[]{"name " + i});
                    break;
                case TESTS_COUNT - 1:
                    testData[i][2] = new LocalFilter("name", Check.Contains, "me " + i);
                    break;
                default:
                    testData[i][2] = new LocalFilter("id", Check.Equal, i);
                    break;
            }

            ((TestModel) testData[i][0]).setName("name " + i);
            ((TestModel) testData[i][1]).setName("name " + i);

            Date currentDate = Calendar.getInstance().getTime();
            ((TestModel) testData[i][0]).setDateValue(currentDate);
            ((TestModel) testData[i][1]).setDateValue(currentDate);

            testData[i][3] = new ArrayList<>();
            if (i == 0) {
                ((List<TestModel>) testData[i][3]).add(new TestModel(i + 10));
                ((List<TestModel>) testData[i][3]).add(new TestModel(i + 11));
            } else if (i == 1) {
                ((List<TestModel>) testData[i][3]).add(new TestModel(i + 12));
                ((List<TestModel>) testData[i][3]).add(new TestModel(i + 13));
            } else if (i == 2) {
                ((List<TestModel>) testData[i][3]).add(new TestModel(i + 14));
                ((List<TestModel>) testData[i][3]).add(new TestModel(i + 15));
            } else if (i == 3) {
                ((List<TestModel>) testData[i][3]).add(new TestModel(i + 16));
                ((List<TestModel>) testData[i][3]).add(new TestModel(i + 17));
            } else if (i == 4) {
                ((List<TestModel>) testData[i][3]).add(new TestModel(i + 18));
                ((List<TestModel>) testData[i][3]).add(new TestModel(i + 19));
            }

        }

        return Arrays.asList(testData);
    }


    @Test
    public void testASave() {
        testRepo.save(actualValue).subscribe(new SingleObserver<TestModel>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onSuccess(@NonNull TestModel testModel) {
                Log.v("tag_info", "Data saving completed");
            }

            @Override
            public void onError(@NonNull Throwable e) {
                fail("Data saving error " + e.getMessage());
            }
        });
    }

    @Test
    public void testFirst() {
        testRepo.first(new LocalFilters().addFilter(localFilterValue), null)
                .subscribe(new SingleObserver<Optional<TestModel>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull Optional<TestModel> testModelWrapper) {
                        assertTrue(testModelWrapper.isPresent());
                        Log.v("tag_info", "Data obtaining completed");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        fail("Data obtaining error " + e.getMessage());
                    }
                });
    }

    @Test
    public void testSavingAndGettingFirstData() {
        testRepo.save(actualValue).flatMap(new Function() {
            @Override
            public Single apply(@NonNull Object o) throws Exception {
                return testRepo.first(new LocalFilters().addFilter(localFilterValue), null);
            }
        }).subscribe(new SingleObserver<Optional<TestModel>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onSuccess(@NonNull Optional<TestModel> actualValue) {
                assertTrue(expectedValue.equals(actualValue.orNull()));
            }

            @Override
            public void onError(@NonNull Throwable e) {
                fail("Data saving error " + e.getMessage());
            }
        });
    }

    @Test
    public void testSaveAll() {
        testRepo.saveAll(dataList).subscribe(new SingleObserver<List<TestModel>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onSuccess(@NonNull List<TestModel> testModel) {
                Log.v("tag_info", "Data saving completed");
            }

            @Override
            public void onError(@NonNull Throwable e) {
                fail("Data saving error " + e.getMessage());
            }
        });
    }

    @Test
    public void testFetch() {
        testRepo.fetch(null, null).subscribe(new SingleObserver<Optional<List<TestModel>>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onSuccess(@NonNull Optional<List<TestModel>> testModelListWrapper) {
                Log.v("tag_info", "Data obtaining completed");
            }

            @Override
            public void onError(@NonNull Throwable e) {
                fail("Data obtaining error " + e.getMessage());
            }
        });
    }

    @Test
    public void testSavingAndGettingFetchData() {
        testRepo.saveAll(dataList).flatMap(new Function() {
            @Override
            public Single apply(@NonNull Object o) throws Exception {
                return testRepo.fetch(null, null);
            }
        }).subscribe(new SingleObserver<Optional<List<TestModel>>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onSuccess(@NonNull Optional<List<TestModel>> actualValue) {
                assertEquals(actualValue.isPresent(), true);
                List<TestModel> testModels = actualValue.get();
                for (int i = 0; i < dataList.size(); i++) {
                    assertTrue(dataList.get(i).equals(testModels.get(i)));
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }
        });
    }

    @Test
    public void testInstantFirst() {
        try {
            testRepo.instantFirst(null, null);
        } catch (Exception exception) {
            fail("Data obtaining error " + exception.getMessage());
        }
    }

    @Test
    public void testSavingAndInstantGettingFirstData() {
        testRepo.save(actualValue).flatMap(new Function() {
            @Override
            public Object apply(@NonNull Object o) throws Exception {
                return Single.just(testRepo.instantFirst(new LocalFilters().addFilter(localFilterValue), null));
            }
        }).subscribe(new SingleObserver<Optional<TestModel>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onSuccess(@NonNull Optional<TestModel> wrapper) {
                assertTrue(expectedValue.equals(wrapper.orNull()));
            }

            @Override
            public void onError(@NonNull Throwable e) {
                fail("Data saving error " + e.getMessage());
            }
        });

    }

    @Test
    public void testInstantFetch() {
        try {
            testRepo.instantFetch(null, null);
        } catch (Exception exception) {
            fail("Data obtaining error " + exception.getMessage());
        }
    }

    @Test
    public void testSavingAndInstantGettingFetchData() {
        testRepo.saveAll(dataList).flatMap(new Function() {
            @Override
            public Object apply(@NonNull Object o) throws Exception {
                return Single.just(testRepo.instantFetch(null, null));
            }
        }).subscribe(new SingleObserver<Optional<List<TestModel>>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onSuccess(@NonNull Optional<List<TestModel>> wrapper) {
                for (int i = 0; i < dataList.size(); i++) {
                    assertTrue(dataList.get(i).equals(wrapper.orNull().get(i)));
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                fail("Data saving error " + e.getMessage());
            }
        });
    }

    @Test
    public void testCount() {
        testRepo.count(null).subscribe(new SingleObserver<Long>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onSuccess(@NonNull Long aLong) {
                Log.v("tag_info", "Success");
            }

            @Override
            public void onError(@NonNull Throwable e) {
                fail("Data counting error " + e.getMessage());
            }
        });
    }

    @Test
    public void testSavingAndCountingData() {
        testRepo.saveAll(dataList).flatMap(new Function() {
            @Override
            public Object apply(@NonNull Object o) throws Exception {
                return testRepo.count(null);
            }
        }).subscribe(new SingleObserver<Long>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onSuccess(@NonNull Long count) {
                assertEquals(dataList.size(), count.intValue());
            }

            @Override
            public void onError(@NonNull Throwable e) {
                fail("Data counting error " + e.getMessage());
            }
        });
    }

    @Test
    public void testInstantCount() {
        try {
            testRepo.instantCount(null);
        } catch (Exception exception) {
            fail("Data counting error " + exception.getMessage());
        }
    }

    @Test
    public void testSavingAndInstantCountingData() {
        testRepo.saveAll(dataList).flatMap(new Function() {
            @Override
            public Object apply(@NonNull Object o) throws Exception {
                return Single.just(testRepo.instantCount(null));
            }
        }).subscribe(new SingleObserver<Long>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onSuccess(@NonNull Long count) {
                assertEquals(dataList.size(), count.intValue());
            }

            @Override
            public void onError(@NonNull Throwable e) {
                fail("Data counting error " + e.getMessage());
            }
        });
    }

    @Test
    public void testRemoveInChain() {
        try {
            testRepo.removeInChain(null).flatMap(new Function<Integer, SingleSource<?>>() {
                @Override
                public SingleSource<?> apply(Integer integer) throws Exception {
                    return Single.just(integer);
                }
            });
        } catch (Exception exception) {
            fail("Data counting error " + exception.getMessage());
        }
    }

}
