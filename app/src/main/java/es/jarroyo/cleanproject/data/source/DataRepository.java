/*
 * Copyright 2016, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package es.jarroyo.cleanproject.data.source;

import android.support.annotation.NonNull;

import static com.google.common.base.Preconditions.checkNotNull;

public class DataRepository implements DataSourceInterface {

    private final DataSourceInterface mDataSourceRemote;
    private static DataRepository INSTANCE = null;



    // Prevent direct instantiation.
    private DataRepository(@NonNull DataRepository dataRemoteDataSource) {
        mDataSourceRemote = dataRemoteDataSource;
    }

    /**
     * Returns the single instance of this class, creating it if necessary.
     */
    public static DataRepository getInstance(DataSourceInterface tasksRemoteDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new DataRepository(tasksRemoteDataSource);
        }
        return INSTANCE;
    }

    public static DataRepository getInstance() {
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    // Prevent direct instantiation.
    public DataRepository(@NonNull DataSourceInterface dataSource) {
        mDataSourceRemote = dataSource;
    }


    @Override
    public void getData(@NonNull LoadDataCallback callback, Double latitude, Double longitud) {
        checkNotNull(callback);


        //FROM REMOTE
        if (mDataSourceRemote != null) {
            mDataSourceRemote.getData(callback, latitude, longitud);
        }
    }

}
