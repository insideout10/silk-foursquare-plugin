<a href="http://insideout.io"><img src="https://insideout10.github.io/silk-foursquare-plugin/images/insideout10-opensource.png" /></a>

Foursquare Silk Plugin
======================

The Foursquare Silk Plugin extends Silk to reconcile with Foursquare data.

Data publishers that need to link their data to 3rd party platforms such as Foursquare can use this plugin in their
existing or new Silk workflow to match Point of Interests (POIs) with Foursquare venues, adding the Foursquare venue id
or URL to their data.

Install
-------

Compile the library to a JAR file and copy it to Silk plugins folder (`~/.silk/plugins`).


Configure
---------

The Foursquare plugin supports the following parameters:

 * *clientId*: the Foursquare client id,
 * *clientSecret*: the Foursquare client secret,
 * *prefix*: the prefix to prepend to the Foursquare venue id (if you want to link to Foursquare web site, use http://foursquare.com/venue/),
 * *limit*: the maximum number of results (use 1 to get the best match).


Using the Workbench
___________________

@@TODO@@

Using the project configuration
_______________________________

Edit or create the `rules.xml` file in Silk's project folder (`~/.silk/workspace/project-name/transform/transform-name/rules.xml`).

The following will load the *Fousquare* plugin and will match Foursquare data using the POI name, locality and country.
It'll return the Foursquare venue URL, such as _http://foursquare.com/venue/xyz_:

    <TransformRules>
      <TransformRule name="Foursquare" targetProperty="&lt;http://schema.org/sameAs&gt;">
        <TransformInput id="reconcile_with_foursquare" function="foursquare">
          <Input id="name" path="?a/&lt;http://www.w3.org/2000/01/rdf-schema#label&gt;[@lang = 'en']"/>
          <Input id="locality" path="?a/&lt;http://schema.org/address&gt;/&lt;http://schema.org/addressLocality&gt;"/>
          <Input id="country" path="?a/&lt;http://schema.org/address&gt;/&lt;http://schema.org/addressCountry&gt;"/>
          <Param name="clientId" value="your-client-id"/>
          <Param name="clientSecret" value="your-client-secret"/>
          <Param name="prefix" value="http://foursquare.com/venue/"/>
          <Param name="limit" value="1"/>
        </TransformInput>
      </TransformRule>
    </TransformRules>


Using Single Machine
___________________________


@@TODO@@
