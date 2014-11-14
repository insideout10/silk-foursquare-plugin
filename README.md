<a href="http://insideout.io"><img src="https://insideout10.github.io/silk-foursquare-plugin/images/insideout10-opensource.png" /></a>

Foursquare Silk Plugin
======================

Create a transform.

How to configure

    <TransformRules>
      <TransformRule name="Foursquare" targetProperty="&lt;http://schema.org/sameAs&gt;">
        <TransformInput id="load_from_foursquare" function="foursquare">
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
