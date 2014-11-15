<a href="http://insideout.io"><img src="https://insideout10.github.io/silk-foursquare-plugin/images/insideout10-opensource.png" /></a>

Foursquare Silk Plugin
======================

The Foursquare Silk Plugin extends Silk to reconcile with Foursquare data.

Data publishers that need to link their data to 3rd party platforms such as Foursquare can use this plugin in their
existing or new Silk workflow to match Point of Interests (POIs) with Foursquare venues, adding the Foursquare venue id
or URL to their data.

*Table of Contents*

 * [Install](#install)
 * [Configure](#configure)
  * [Using the Workbench](#using-the-workbench)
  * [Using the project configuration](#using-the-project-configuration)
  * [Using Single Machine](#using-single-machine)

## Install

Compile the library to a JAR file and copy it to Silk plugins folder (`~/.silk/plugins`).


## Configure

The Foursquare plugin supports the following parameters:

 * *clientId*: the Foursquare client id,
 * *clientSecret*: the Foursquare client secret,
 * *prefix*: the prefix to prepend to the Foursquare venue id (if you want to link to Foursquare web site, use http://foursquare.com/venue/),
 * *limit*: the maximum number of results (use 1 to get the best match).


If you don't have a Foursquare client id and secret, you can create one here: https://foursquare.com/developers/apps


### Using the Workbench

The Foursquare plugin can be used both in *transforms* and in *linking*. The following example covers the *transform*
configuration.

Create a new transform by clicking the *transform* button:

<img src="https://insideout10.github.io/silk-foursquare-plugin/images/workbench_step_1.png" />

Name the transform, choose a source dataset and set a restriction as usual:

<img src="https://insideout10.github.io/silk-foursquare-plugin/images/workbench_step_2.png" />

Once created, open the transform by clicking on the *open* button:

<img src="https://insideout10.github.io/silk-foursquare-plugin/images/workbench_step_3.png" />

The editor will open, click on the *Add Rule* button:

<img src="https://insideout10.github.io/silk-foursquare-plugin/images/workbench_step_4.png" />

An empty rule is created:

<img src="https://insideout10.github.io/silk-foursquare-plugin/images/workbench_step_5.png" />

Give a name to the rule, e.g. _load_from_foursquare_:

<img src="https://insideout10.github.io/silk-foursquare-plugin/images/workbench_step_6.png" />

Set the target property, e.g. _schema:sameAs_ (ensure schema is defined in your prefixes, or it won't work). Then click
on the wrench icon on the top right corner:

<img src="https://insideout10.github.io/silk-foursquare-plugin/images/workbench_step_7.png" />

The editor will open:

<img src="https://insideout10.github.io/silk-foursquare-plugin/images/workbench_step_8.png" />

In the transformations box, choose the *API* category. The Foursquare component will show:

<img src="https://insideout10.github.io/silk-foursquare-plugin/images/workbench_step_9.png" />

Add the source paths, one by one, in our example:

 1. `?a/rdfs:label[?lang = 'en']`
 2. `?a/schema:address/schema:addressLocality`
 3. `?a/schema:address/schema:addressCountry`

Add the Foursquare component.

Connect the source paths, in the exact same order as above, to the Foursquare component. *It is important* to connect
 the source paths in the order of *name, locality and country* because the component uses them in that order.

Connect the *name*:

<img src="https://insideout10.github.io/silk-foursquare-plugin/images/workbench_step_10.png" />

Connect the *locality*:

<img src="https://insideout10.github.io/silk-foursquare-plugin/images/workbench_step_11.png" />

Connect the *country*:

<img src="https://insideout10.github.io/silk-foursquare-plugin/images/workbench_step_12.png" />

Set the Foursquare component parameters, including your client id and secret. For prefix we suggest to use http://foursquare.com/venue/ and for limit 1:

<img src="https://insideout10.github.io/silk-foursquare-plugin/images/workbench_step_13.png" />

Click on the evaluate tab, the process will start. Be patient it might take some time especially if you have thousands
 of items (for each item a request to Foursquare is made). Some progress information are written to the console log.

At the end you'll see the list of the results:

<img src="https://insideout10.github.io/silk-foursquare-plugin/images/workbench_step_14.png" />

In this example, the Schlosscafé in Matsee, Austria has been reconciled with its Foursquare venue URL:

<img src="https://insideout10.github.io/silk-foursquare-plugin/images/workbench_step_15.png" />


### Using the project configuration

Edit or create the `rules.xml` file in Silk's project folder (`~/.silk/workspace/project-name/transform/transform-name/rules.xml`).

The following will load the *Fousquare* plugin and will match Foursquare data using the POI name, locality and country.
It'll return the Foursquare venue URL, such as _http://foursquare.com/venue/xyz_.

The order of the `Input` elements *is important*, they must be listed in the following order:

 1. POI name
 2. locality
 3. country


    <TransformRules>
      <TransformRule name="Foursquare" targetProperty="&lt;http://schema.org/sameAs&gt;">
        <TransformInput id="reconcile_with_foursquare" function="foursquare">
          <Input id="name" path="?a/&lt;http://www.w3.org/2000/01/rdf-schema#label&gt;[@lang = 'en']"/>
          <Input id="locality" path="?a/&lt;http://schema.org/address&gt;/&lt;http://schema.org/addressLocality&gt;"/>
          <Input id="country" path="?a/&lt;http://schema.org/address&gt;/&lt;http://schema.org/addressCountry&gt;"/>
          <Param name="clientId" value="your-foursquare-client-id"/>
          <Param name="clientSecret" value="your-foursquare-client-secret"/>
          <Param name="prefix" value="http://foursquare.com/venue/"/>
          <Param name="limit" value="1"/>
        </TransformInput>
      </TransformRule>
    </TransformRules>


### Using Single Machine

Following is an example of a `Transform` element that can be configured in a [Silk LSL](https://www.assembla.com/wiki/show/silk/Link_Specification_Language)
configuration file for use with Single Machine.

The order of the `Input` elements *is important*, they must be listed in the following order:

 1. POI name
 2. locality
 3. country


*Note that for this to work, Silk framework needs [pull request 29](https://github.com/silk-framework/silk/pull/29)*


    <Silk>

      <!-- Your existing configuration ... -->

      <Transforms>
          <Transform id="reconcile-with-foursquare">
              <SourceDataset dataSource="your-source-dataset" var="a">
                  <!-- Restrict to businesses. -->
                  <RestrictTo>?a a &lt;http://schema.org/LocalBusiness&gt;</RestrictTo>
              </SourceDataset>

              <TransformRule name="Foursquare" targetProperty="&lt;http://schema.org/sameAs&gt;">
                  <TransformInput id="load_from_foursquare" function="foursquare">
                      <Input id="name" path="?a/&lt;http://www.w3.org/2000/01/rdf-schema#label&gt;[@lang = 'en']"/>
                      <Input id="locality"
                             path="?a/&lt;http://schema.org/address&gt;/&lt;http://schema.org/addressLocality&gt;"/>
                      <Input id="country"
                             path="?a/&lt;http://schema.org/address&gt;/&lt;http://schema.org/addressCountry&gt;"/>
                      <Param name="clientId" value="your-foursquare-client-id"/>
                      <Param name="clientSecret" value="your-foursquare-secret-id"/>
                      <Param name="prefix" value="http://foursquare.com/venue/"/>
                      <Param name="limit" value="1"/>
                  </TransformInput>
              </TransformRule>

              <Outputs>

                <!-- Your outputs list... -->

              </Outputs>

          </Transform>
      </Transforms>

    </Silk>
