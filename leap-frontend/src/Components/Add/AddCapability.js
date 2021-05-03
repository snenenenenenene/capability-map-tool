import React, {Component} from 'react';
import {Link} from 'react-router-dom';
import RecentEnvironmentTableRow from "../RecentEnvironmentTableRow";

export default class AddCapability extends Component {
    constructor(props) {
        super(props);
        this.state = {
            environments: [],
            environmentName: this.props.match.params.name,
            environmentId: 1,
            capabilityName: '',
            parentCapability: '',
            description: '',
            paceOfChange: '',
            TOM: '',
            informationQuality: '',
            applicationFit: '',
            resourcesQuality: '',
            expirationDate: '',
            level: 0,
        };
        this.handleSubmit = this.handleSubmit.bind(this);
        this.handleInputChange = this.handleInputChange.bind(this)
    }

    handleSubmit = async e => {
        e.preventDefault();
        console.log(this.state.environmentName)
        const post_response = await fetch(`http://localhost:8080/capability/add`, { method: 'POST',
            headers:
                {
                    'Content-Type': "application/json",
                    'Accept': "application/json"},
            body: JSON.stringify({
                environment: {
                    environmentName: this.state.environmentName,
                    environmentId: this.state.environmentId
                },
                capabilityName: this.state.capabilityName,
                parentCapabilityId: this.state.parentCapability,
                paceOfChange: this.state.paceOfChange,
                targetOperatingModel: this.state.TOM,
                informationQuality: this.state.informationQuality,
                applicationFit: this.state.applicationFit,
                resourceQuality: this.state.resourcesQuality,
                // status: this.state.expirationDate,
                level: this.state.level
            }) });
        if (!post_response.ok) {
            console.log('Failed to upload via presigned POST');
        }
        console.log(`File uploaded via presigned POST with key: ${this.state.capabilityName}`);
    }

    componentDidMount() {
    }

    handleInputChange(event) {
        this.setState({ [event.target.name]: event.target.value })
    }

    render() {
        const environmentName = this.props.match.params.name;
        return (
            <div>
                <nav aria-label="breadcrumb">
                    <ol className="breadcrumb">
                        <li className="breadcrumb-item"><Link to={`/`}><a>Home</a></Link></li>
                        <li className="breadcrumb-item"><Link to={`/environment/${environmentName}`}><a>{environmentName}</a></Link></li>
                        <li className="breadcrumb-item active" aria-current="page">Add Capability</li>
                    </ol>
                </nav>
            <div class="jumbotron">
                <h3>Add Capability</h3>
                <form onSubmit={this.handleSubmit}>
                    <div className="row">
                        <div className="col-sm-6">
                            <div className="form-row">
                                <div className="form-group col-md-6">
                                    <label htmlFor="nameCapability">Name Capability</label>
                                <input type="text" id="capabilityName" name="capabilityName" className="form-control" placeholder="Name Capability"
                                       value={this.state.capabilityName} onChange={this.handleInputChange}/>
                                </div>
                            </div>
                            <div className="form-row">
                                <div className="form-group col-md-6">
                                    <label htmlFor="paceOfChange">Parent Capability</label>
                                <select className="form-control" name="parentCapability" id="parentCapability" placeholder="Add Parent Capability"
                                        value={this.state.parentCapabilityId} onChange={this.handleInputChange}>
                                    <option value="1">Capability 1</option>
                                    <option value="2">Capability 2</option>
                                    <option value="3">Capability 3</option>
                                </select>
                                </div>
                                <div className="form-group col-md-6">
                                    <label htmlFor="capabilityLevel">Capability Level</label>
                                <select className="form-control" name="capabilityLevel" id="capabilityLevel" placeholder="Add Level"
                                        value={this.state.capabilityLevel} onChange={this.handleInputChange}>
                                    <option>1</option>
                                    <option>2</option>
                                    <option>3</option>
                                </select>
                                </div>
                            </div>
                            <div className="form-group">
                                <label htmlFor="description">Description</label>
                                <textarea type="text" id="description" name="description" className="form-control" rows="4" placeholder="Description"
                                          value={this.state.description} onChange={this.handleInputChange}/>
                            </div>
                        </div>
                        <div className="col-sm-6">
                            <div className="form-row">
                                <div className="form-group col-md-6">
                                    <label htmlFor="paceOfChange">Pace of Change</label>
                                    <select className="form-control" name="paceOfChange" placeholder="Add Pace of Change" id="paceOfChange"
                                            value={this.state.paceOfChange} onChange={this.handleInputChange}>
                                        <option>True</option>
                                        <option>False</option>
                                    </select>
                                </div>
                                <div className="form-group col-md-6">
                                    <label htmlFor="informationQuality">Information Quality</label>
                                    <select className="form-control" name="informationQuality" placeholder="Add Information Quality" id="informationQuality"
                                            value={this.state.informationQuality} onChange={this.handleInputChange}>
                                        <option>1</option>
                                        <option>2</option>
                                        <option>3</option>
                                        <option>4</option>
                                        <option>5</option>
                                    </select>
                                </div>
                            </div>
                            <div className="form-row">
                                <div className="form-group col-md-6">
                                    <label htmlFor="paceOfChange">TOM</label>
                                    <select className="form-control" name="TOM" placeholder="Add TOM" id="TOM"
                                            value={this.state.TOM} onChange={this.handleInputChange}>
                                        <option>1</option>
                                        <option>2</option>
                                        <option>3</option>
                                    </select>
                                </div>
                                <div className="form-group col-md-6">
                                    <label htmlFor="applicationFit">Application Fit</label>
                                    <select className="form-control" placeholder="Add Application Fit" id="applicationFit"
                                            ref={input => (this.state.applicationFit = input)}>
                                        <option>1</option>
                                        <option>2</option>
                                        <option>3</option>
                                        <option>4</option>
                                        <option>5</option>
                                    </select>
                                </div>
                            </div>

                            <div className="form-group">
                                <label for="resourcesQuality">Resources Quality</label>
                                <select id="resourcesQuality" name="resourcesQuality" className="form-control" placeholder="Resources Quality"
                                        value={this.state.resourcesQuality} onChange={this.handleInputChange}>
                                    <option>1</option>
                                    <option>2</option>
                                    <option>3</option>
                                    <option>4</option>
                                    <option>5</option>
                                </select>
                                <label htmlFor="expirationDate">Expiration Date</label>
                                <input id="expirationDate" name="expirationDate" type="date" className="form-control"
                                       value={this.state.expirationDate} onChange={this.handleInputChange}/>
                            </div>
                        </div>
                    </div>
                    <button className="btn btn-primary" type="button" onClick={this.handleSubmit}>Submit</button>
                </form>
            </div>
            </div>
        )
    }
}