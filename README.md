<strong>Aims of the App</strong>

As I am enrolled on the HDSD course and there is a requirement to choose a workplace-oriented final year project, I plan to build a simple life coaching app. In many workplaces, some service is offered to help with work-life balance. Some workplaces have subscriptions to mindfulness apps or life coaching services available to employees. My app will aim to provide work-life balance by allowing users to identify important areas of their lives and record actions they've taken to improve those areas.

My app will use Fragments and will have three tabs. The tab titles will be "Life Areas", "Actions" and "Chart".

In the Life Areas tab, the user will be able to choose the life areas to focus on. This tab will contain six EditText views and a Save button. The first time the app is used, the EditText views will be populated with default life areas: health, work, family, friends, finances and learning. The user can personalise these values. There will be a Save button under the EditTexts. Choosing Save will save the life areas in SharedPreferences. These life areas can be changed again by the user or reset to defaults.

The Actions tab will allow users to record actions they've completed linked to each life area. This tab will display a drop-down menu, populated with the life areas. A user will be prompted to choose life areas if this hasn't been done yet and the drop-down menu will contain only areas of focus that have no action already taken that day. Once a focus area has been chosen, a multi-line EditText will appear below the drop-down menu, where a summary of the action taken can be written. For example, a user could enter "made a budget" for finances or "stopped working at 5.30 to help my son with his homework" for family.

The action taken by the user will be saved in a Cloud Firestore database to facilitate the suggestions that will be displayed above the drop-down menu in the Actions tab. When a life area is becoming neglected, a suggestion will be displayed. The suggestion will reference the life area that needs attention and offer a suggestion taken from the Cloud Firestore database. For example, the summary text might say "One area that needs attention is *health*. A past action you've taken in this area is: *got 8 hours of sleep*."

The Chart tab will display a radar chart, plotted with the six life areas the user is working on. Data for this chart will be calculated using a Firebase query, which will check how many documents (actions) have been stored in each collection (life area).

<strong>Methodology and Android APIs</strong>

<ins>Networks:</ins> I plan to store life area actions in a Cloud Firestore database.

<ins>Multimedia:</ins> I would like to explore graphing and I plan to use MPAndroidChart for this. I will use a radar chart to display which life areas need attention.
  
