const axios = require("axios");
const { JwtGenerator } = require("virgil-sdk");
const { initCrypto, VirgilCrypto, VirgilAccessTokenSigner } = require("virgil-crypto");

const APP_ID = "8b8d3c6a1bf7459b854d5b99a4013beb";
const APP_KEY = "MC4CAQAwBQYDK2VwBCIEIC9eFMqeNLuHGwXAy3FxGdAQu9XLzI3T51dY7L9RJmAZ";
const APP_KEY_ID = "a994f9ed651f0c975cb2bca0fe4317fc";
const PARSE_APP_ID = "DmI1r1lm4UChhY3qr1zvC2zoQOFmV85bnR1Dt1pY";
const PARSE_REST_API_KEY = "dupUhtlmideK7jAWIBHQV6XYGKvjueI5rGrN5LpQ";

initCrypto().then(() => {
  const crypto = new VirgilCrypto();
  const generator = new JwtGenerator({
      appId: APP_ID,
      apiKeyId: APP_KEY_ID,
      apiKey: crypto.importPrivateKey(APP_KEY),
      accessTokenSigner: new VirgilAccessTokenSigner(crypto)
  });

  Parse.Cloud.define("virgil-jwt", (request) => {
    const { sessionToken } = request.params;
    return axios
      .get("https://parseapi.back4app.com/users/me", {
        headers: {
          "X-Parse-Application-Id": PARSE_APP_ID,
          "X-Parse-REST-API-Key": PARSE_REST_API_KEY,
          "X-Parse-Session-Token": sessionToken
        }
      })
      .then(resp => {
        const identity = resp.data.objectId;
        const virgilJwtToken = generator.generateToken(identity);
        const tokenStr = virgilJwtToken.toString();

        return { token: tokenStr };
      })
      .catch(error => {
        throw new Error(error.message);
      });
  });
});
