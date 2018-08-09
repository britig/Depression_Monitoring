# Depression_Monitoring

A Depression Monitoring System which will analyse the daily activities and other parameters such as location , exposure to light etc of an user and based on the data collected over a period of time will be able to predict   whether the user is depressed or not.


The data is trained with data from Jennifer R. Kwapisz, Gary M. Weiss and Samuel A. Moore (2010). Activity Recognition using Cell Phone Accelerometers,Proceedings of the Fourth International Workshop on Knowledge Discovery from Sensor Data (at KDD-10), Washington DC.
for training the data for recognizing different activities. The trained model is then deployed in the android app.

The model records activities and stores the collected data in firebase database over cloud. It fits a logistic rgression model over this collected data along with other parameters to predict depression.

The machine learning codes are available in Depression_Monitoring.ipnyb


