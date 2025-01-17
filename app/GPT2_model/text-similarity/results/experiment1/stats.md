### Similarity Fine-Tune Iteration 0   

    [400/400 06:50, Epoch 10/10]
    Epoch	Training Loss	Validation Loss
    1	1.874800	1.858043
    2	2.314700	3.382994
    3	0.654000	1.512425
    4	0.392900	0.970662
    5	0.445700	0.907943
    6	0.329200	0.980849
    7	0.241200	0.966972
    8	0.293400	0.919348
    9	0.223600	0.900285
    10	0.190000	0.859607
    TrainOutput(global_step=400, training_loss=1.3047271746397018, metrics={'train_runtime': 411.5624, 'train_samples_per_second': 3.888, 'train_steps_per_second': 0.972, 'total_flos': 418071026073600.0, 'train_loss': 1.3047271746397018, 'epoch': 10.0})

    True Labels: [1.  0.  1.  0.  1.  0.2 0.  1.  0.  1. ]
    Predicted Scores: [0.93935204 0.7352467  0.58311635 0.57003516 0.56850576 0.5595671
     0.6139803  0.58951825 0.8555883  0.51852065]
    Evaluation Metrics:
    Mean Squared Error (MSE): 0.2738
    Mean Absolute Error (MAE): 0.4924
    R-squared (R²): -0.1848
    Pearson Correlation Coefficient: 0.0113
    Spearman Rank Correlation: -0.0568

### Similarity Fine-Tune Iteration 1.1 Without English Vocab Fine-Tune

    Generated 184 labeled pairs.

    [420/420 02:06, Epoch 10/10]
    Epoch	Training Loss	Validation Loss
    1	0.936500	0.519706
    2	0.540200	0.401191
    3	0.298500	0.284789
    4	0.197000	0.435183
    5	0.221300	0.307765
    6	0.152800	0.247429
    7	0.310100	0.273505
    8	0.178800	0.264594
    9	0.193000	0.255846
    10	0.183800	0.261223
    TrainOutput(global_step=420, training_loss=0.7466770524070376, metrics={'train_runtime': 126.9952, 'train_samples_per_second': 12.993, 'train_steps_per_second': 3.307, 'total_flos': 431135745638400.0, 'train_loss': 0.7466770524070376, 'epoch': 10.0})
    
    True Labels: [1.  0.2 0.  1.  0.2 0.  1.  0.2 0.  1. ]
    Predicted Scores: [0.55310035 0.59566253 0.57744825 0.59357655 0.5168786  0.6540009
     0.6675477  0.48706627 0.56039804 0.57377034]
    Evaluation Metrics:
    Mean Squared Error (MSE): 0.2388
    Mean Absolute Error (MAE): 0.4742
    R-squared (R²): -0.1840
    Pearson Correlation Coefficient: -0.0552
    Spearman Rank Correlation: -0.1455

### Similarity Fine-Tune Iteration 1.2 With English Vocab Fine-Tune on 180 instances

    Generated 180 labeled pairs.

    [410/410 02:03, Epoch 10/10]
    Epoch	Training Loss	Validation Loss
    1	0.828200	0.866146
    2	0.575000	0.555228
    3	0.366900	0.581568
    4	0.339200	0.462900
    5	0.304000	0.459458
    6	0.307000	0.451446
    7	0.246700	0.443058
    8	0.231700	0.440297
    9	0.245700	0.421424
    10	0.201500	0.428694
    TrainOutput(global_step=410, training_loss=0.5242244990860544, metrics={'train_runtime': 124.5217, 'train_samples_per_second': 13.01, 'train_steps_per_second': 3.293, 'total_flos': 423296913899520.0, 'train_loss': 0.5242244990860544, 'epoch': 10.0})
    
    True Labels: [1.  0.2 0.  1.  0.  1.  0.  1.  0.  1. ]
    Predicted Scores: [0.55406594 0.6559527  0.5764712  0.54564875 0.5785231  0.63090456
     0.6178102  0.59490615 0.6796177  0.54430574]
    Evaluation Metrics:
    Mean Squared Error (MSE): 0.2757
    Mean Absolute Error (MAE): 0.5171
    R-squared (R²): -0.1930
    Pearson Correlation Coefficient: -0.4907
    Spearman Rank Correlation: -0.4770

==============================================
### English Fine-Tune Iteration 2

    Original train size: 120000
    Limited train size: 1200

    [750/750 08:34, Epoch 10/10]
    Epoch	Training Loss	Validation Loss
    1	No log	0.789410
    2	No log	0.763629
    3	No log	0.757206
    4	No log	0.757422
    5	No log	0.760376
    6	No log	0.763089
    7	0.803200	0.769366
    8	0.803200	0.772763
    9	0.803200	0.775875
    10	0.803200	0.777806
    
Test:

- Original GPT-2: Left or right? The answer is that the right is the most important thing. It is the most important thing to you. It is the most important thing to your family. It is the most important thing to your friends. It is the
- Fine-tuned GPT-2: Left or right? The difference between the two is that the left is more likely to be a good player and the right more likely to be a bad one.

### Similarity Fine-Tune Iteration 2

    Selected 287 examples across 15 categories.
    Generated 844 labeled pairs.

    [1900/1900 09:39, Epoch 10/10]
    Epoch	Training Loss	Validation Loss
    1	0.216900	0.283091
    2	0.223200	0.219194
    3	0.185300	0.209125
    4	0.103200	0.179101
    5	0.075300	0.163590
    6	0.042000	0.141489
    7	0.075100	0.147197
    8	0.057700	0.148141
    9	0.086500	0.133102
    10	0.027000	0.131803
    TrainOutput(global_step=1900, training_loss=0.1858141325022045, metrics={'train_runtime': 579.9353, 'train_samples_per_second': 13.088, 'train_steps_per_second': 3.276, 'total_flos': 1983224429936640.0, 'train_loss': 0.1858141325022045, 'epoch': 10.0})

    True Labels: [0.  1.  0.2 0.  1.  0.2 0.  1.  0.2 0. ]
    Predicted Scores: [0.51235706 0.72376645 0.49569553 0.52597064 0.50665265 0.57933176
     0.5255884  0.5414602  0.58717614 0.5490705 ]
    Evaluation Metrics:
    Mean Squared Error (MSE): 0.1882
    Mean Absolute Error (MAE): 0.4199
    R-squared (R²): 0.0174
    Pearson Correlation Coefficient: 0.6007
    Spearman Rank Correlation: 0.4798

