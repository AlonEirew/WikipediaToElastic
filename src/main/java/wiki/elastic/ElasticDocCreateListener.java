/**
 * @author  Alon Eirew
 */

package wiki.elastic;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.index.IndexResponse;

public class ElasticDocCreateListener implements ActionListener<IndexResponse> {

    private final static Logger LOGGER = LogManager.getLogger(ElasticDocCreateListener.class);

    private final ElasticAPI elasicApi;

    public ElasticDocCreateListener(ElasticAPI elasicApi) {
        this.elasicApi = elasicApi;
    }

    @Override
    public void onResponse(IndexResponse indexResponse) {
        this.elasicApi.releaseSemaphore();
        String index = indexResponse.getIndex();
        String id = indexResponse.getId();
        if (indexResponse.getResult() == DocWriteResponse.Result.CREATED) {
            LOGGER.trace("document with id:" + id + " Created successfully at index:" + index);
        } else if (indexResponse.getResult() == DocWriteResponse.Result.UPDATED) {
            LOGGER.trace("document with id:" + id + " Updated successfully at " + index);
        }
    }

    @Override
    public void onFailure(Exception e) {
        this.elasicApi.releaseSemaphore();
        LOGGER.error("failed inserting document with exception!", e);
    }
}
