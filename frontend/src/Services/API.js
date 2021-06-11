import axios from "axios";

export default class API {
  constructor() {
    this.url = process.env.REACT_APP_API_URL;
    this.endpoints = {};
    this.jwt = this.jwtCheck();
    this.email = this.emailCheck();
  }

  jwtCheck() {
    if (localStorage.getItem("user"))
      return JSON.parse(localStorage.getItem("user")).jwt;
    return null;
  }
  emailCheck() {
    if (localStorage.getItem("user"))
      return JSON.parse(localStorage.getItem("user")).email;
    return null;
  }
  createEntity(entity) {
    this.endpoints[entity.name] = this.CRUDEndpoints(entity);
  }

  createEntities(arrayOfEntity) {
    arrayOfEntity.forEach(this.createEntity.bind(this));
  }

  CRUDEndpoints({ name }) {
    var endpoints = {};
    const config = {
      headers: {
        Authorization: `Bearer ${this.jwt}`,
      },
    };
    const resourceURL = `${this.url}/${name}`;

    //USER
    endpoints.updateUser = (toUpdate) =>
      axios.put(`${resourceURL}/`, toUpdate, config);

    //GENERAL
    endpoints.getAll = ({ query } = {}) => axios.get(`${resourceURL}/`, config);
    endpoints.getCurrencies = ({ query } = {}) =>
      axios.get(`${resourceURL}/all-currencies`, config);
    endpoints.getOne = ({ id }) => axios.get(`${resourceURL}/${id}`, config);
    endpoints.getCapabilities = ({ id }) =>
      axios.get(`${resourceURL}/get-capabilities/${id}`, config);
    endpoints.linkProject = (toCreate) =>
      axios.put(`${resourceURL}/link-project/`, toCreate, config);
    endpoints.linkCapabilityApplication = ({
      capabilityId,
      itApplicationId,
      form,
    }) =>
      axios.put(
        `${resourceURL}/${capabilityId}/${itApplicationId}`,
        form,
        config
      );
    endpoints.linkResource = (toCreate) =>
      axios.put(`${resourceURL}/link-resource/`, toCreate, config);

    endpoints.getAllCapabilitiesByApplicationId = ({ id }) =>
      axios.get(
        `${resourceURL}/all-capabilityApplications-by-applicationid/${id}`,
        config
      );

    endpoints.getAllCapabilitiesByInformationId = ({ id }) =>
      axios.get(
        `${resourceURL}/all-capabilityinformation-by-informationid/${id}`,
        config
      );

    endpoints.linkBusinessProcess = (toCreate) =>
      axios.put(`${resourceURL}/link-businessprocess/`, toCreate, config);

    endpoints.importCSV = (toCreate) =>
      axios.post(`${resourceURL}/upload-csv-file`, toCreate, config);
    endpoints.getUser = ({ query } = {}) =>
      axios.get(`${resourceURL}/email/${this.email}`, config);

    //ENVIRONMENT
    endpoints.getEnvironmentByName = ({ name }) =>
      axios.get(`${resourceURL}/environmentname/${name}`, config);
    endpoints.environmentExists = ({ name }) =>
      axios.get(`${resourceURL}/exists-by-environmentname/${name}`, config);
    endpoints.create = (toCreate) =>
      axios.post(`${resourceURL}/`, toCreate, config);
    endpoints.update = (toUpdate, id) =>
      axios.put(`${resourceURL}/${id}`, toUpdate, config);
    endpoints.delete = ({ id }) => axios.delete(`${resourceURL}/${id}`, config);
    endpoints.unlink = ({ capabilityId, id }) =>
      axios.delete(`${resourceURL}/${capabilityId}/${id}`, config);
    endpoints.unlinkProject = ({ capabilityId, id }) =>
      axios.delete(
        `${resourceURL}/unlink-project/${capabilityId}/${id}`,
        config
      );
    endpoints.unlinkResource = ({ capabilityId, id }) =>
      axios.delete(
        `${resourceURL}/unlink-resource/${capabilityId}/${id}`,
        config
      );
    endpoints.unlinkBusinessProcess = ({ capabilityId, id }) =>
      axios.delete(
        `${resourceURL}/unlink-businessprocess/${capabilityId}/${id}`,
        config
      );
    endpoints.generateCapabilityMap = ({ id }) =>
      axios.get(`${resourceURL}/capabilitymap/${id}`, config);
    endpoints.getAllCapabilitiesByEnvironmentId = ({ id }) =>
      axios.get(
        `${resourceURL}/all-capabilities-by-environmentid/${id}`,
        config
      );

    endpoints.getAllCapabilityItemsByCapabilityId = ({ id }) =>
      axios.get(
        `${resourceURL}/all-capabilityitems-by-capabilityid/${id}`,
        config
      );
    endpoints.getAllCapabilityItemsByItemId = ({ id }) =>
      axios.get(
        `${resourceURL}/all-capabilityitems-by-strategyitemid/${id}`,
        config
      );
    return endpoints;
  }
}
