import React, {Component} from 'react';
import {Link} from 'react-router-dom';
import RecentEnvironmentTableRow from "../RecentEnvironmentTableRow";

export default class AddResources extends Component {
    constructor(props) {
        super(props);
        this.state = {
            environments: [],
            environmentName: '',
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
    }

    async handleSubmit() {
        var Capability = {
            environment: this.state.environmentName,
            capabilityName: this.state.capabilityName.value,
            parentCapabilityId: this.state.parentCapability.value,
            // description: this.state.description.value,
            paceOfChange: this.state.paceOfChange.value,
            targetOperatingModel: this.state.TOM.value,
            informationQuality: this.state.informationQuality.value,
            applicationFit: this.state.applicationFit.value,
            resourceQuality: this.state.resourcesQuality.value,
            status: this.state.expirationDate.value,
            level: this.state.level.value
        }
        console.log(Capability)
        const post_response = await fetch(`http://localhost:8080/strategy/add`, { method: 'POST', body: Capability });
        if (!post_response.ok) {
            console.log('Failed to upload via presigned POST');
        }
        console.log(`File uploaded via presigned POST with key: ${Capability.capabilityName}`);
    }

    componentDidMount() {
    }

    render() {
        const environmentName = this.props.match.params.name;
        return (
            <div>
                <nav aria-label="breadcrumb">
                    <ol className="breadcrumb">
                        <li className="breadcrumb-item"><Link to={`/`}><a>Home</a></Link></li>
                        <li className="breadcrumb-item"><Link to={`/environment/${environmentName}`}><a>{environmentName}</a></Link></li>
                        <li className="breadcrumb-item active" aria-current="page">Add Resources</li>
                    </ol>
                </nav>
                <div class="jumbotron">
                    <h3>Add Resources</h3>
                    <form onSubmit={this.handleSubmit} method="POST">
                        <div className="row">
                            <div className="col-sm-6">
                                <div className="form-row">
                                    <div className="form-group col-md-6">
                                        <label htmlFor="nameCapability">Name Capability</label>
                                        <input type="text" id="capabilityName" className="form-control" placeholder="Name Capability"
                                               ref={input => (this.state.capabilityName = input)}></input>
                                    </div>
                                </div>
                                <div className="form-row">
                                    <div className="form-group col-md-6">
                                        <label htmlFor="paceOfChange">Parent Capability</label>
                                        <select className="form-control" id="parentCapability" placeholder="Add Parent Capability"
                                                ref={input => (this.state.parentCapability = input)}>
                                            <option>Capability 1</option>
                                            <option>Capability 2</option>
                                            <option>Capability 3</option>
                                        </select>
                                    </div>
                                    <div className="form-group col-md-6">
                                        <label htmlFor="capabilityLevel">Capability Level</label>
                                        <select className="form-control" id="capabilityLevel" placeholder="Add Level"
                                                ref={input => (this.state.level = input)}>
                                            <option>1</option>
                                            <option>2</option>
                                            <option>3</option>
                                        </select>
                                    </div>
                                </div>
                                <div className="form-group">
                                    <label htmlFor="description">Description</label>
                                    <textarea type="text" id="description" className="form-control" rows="4" placeholder="Description"
                                              ref={input => (this.state.description = input)}></textarea>
                                </div>
                            </div>
                            <div className="col-sm-6">
                                <div className="form-row">
                                    <div className="form-group col-md-6">
                                        <label htmlFor="paceOfChange">Pace of Change</label>
                                        <select className="form-control" placeholder="Add Pace of Change" id="paceOfChange"
                                                ref={input => (this.state.paceOfChange = input)}>
                                            <option>True</option>
                                            <option>False</option>
                                        </select>
                                    </div>
                                    <div className="form-group col-md-6">
                                        <label htmlFor="informationQuality">Information Quality</label>
                                        <select className="form-control" placeholder="Add Information Quality" id="informationQuality"
                                                ref={input => (this.state.informationQuality = input)}>
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
                                        <select className="form-control" placeholder="Add TOM" id="TOM"
                                                ref={input => (this.state.TOM = input)}>
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
                                    <select id="resourcesQuality" className="form-control" placeholder="Resources Quality"
                                            ref={input => (this.state.resourcesQuality = input)}>
                                        <option>1</option>
                                        <option>2</option>
                                        <option>3</option>
                                        <option>4</option>
                                        <option>5</option>
                                    </select>
                                    <label htmlFor="expirationDate">Expiration Date</label>
                                    <input id="expirationDate" type="date" className="form-control"
                                           ref={input => (this.state.expirationDate = input)}></input>
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